package com.pilotbooking.service.impl;

import com.pilotbooking.domain.Booking;
import com.pilotbooking.domain.Payment;
import com.pilotbooking.domain.ServiceFacility;
import com.pilotbooking.domain.User;
import com.pilotbooking.domain.enums.BookingStatus;
import com.pilotbooking.domain.enums.PaymentStatus;
import com.pilotbooking.repository.BookingRepository;
import com.pilotbooking.repository.PaymentRepository;
import com.pilotbooking.repository.ServiceFacilityRepository;
import com.pilotbooking.repository.UserRepository;
import com.pilotbooking.service.BookingService;
import com.pilotbooking.service.FileStorageService;
import com.pilotbooking.web.dto.request.BookingRequest;
import com.pilotbooking.web.dto.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceFacilityRepository serviceRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public BookingResponse createSoftBooking(BookingRequest request, String userEmail) {
        return processBooking(request, userEmail, BookingStatus.SOFT_BOOKED, null);
    }

    @Override
    @Transactional
    public BookingResponse createHardBooking(BookingRequest request, MultipartFile proofImage, String userEmail) {
        return processBooking(request, userEmail, BookingStatus.PENDING_APPROVAL, proofImage);
    }

    @Override
    public List<Map<String, LocalDate>> getUnavailableDates(UUID serviceId) {
        ServiceFacility service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        List<BookingStatus> hardStatuses = Arrays.asList(
                BookingStatus.PENDING_APPROVAL,
                BookingStatus.HARD_BOOKED
        );

        List<Booking> bookings = bookingRepository.findActiveBookingsForService(serviceId, hardStatuses, LocalDate.now());

        Map<LocalDate, Long> dailyCounts = new HashMap<>();
        for (Booking b : bookings) {
            for (LocalDate d = b.getStartDate(); !d.isAfter(b.getEndDate()); d = d.plusDays(1)) {
                dailyCounts.put(d, dailyCounts.getOrDefault(d, 0L) + 1);
            }
        }

        List<Map<String, LocalDate>> unavailableDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entry : dailyCounts.entrySet()) {
            if (entry.getValue() >= service.getQuota()) {
                Map<String, LocalDate> dateMap = new HashMap<>();
                dateMap.put("date", entry.getKey());
                unavailableDates.add(dateMap);
            }
        }
        return unavailableDates;
    }

    @Override
    public List<BookingResponse> getAllBookingsForAdmin() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatus(UUID bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (newStatus == BookingStatus.HARD_BOOKED &&
                (booking.getStatus() == BookingStatus.SOFT_BOOKED || booking.getStatus() == BookingStatus.PENDING_APPROVAL)) {
            validateDateAvailability(booking.getService(), booking.getStartDate(), booking.getEndDate());
        }

        booking.setStatus(newStatus);
        Booking updatedBooking = bookingRepository.save(booking);

        Optional<Payment> optionalPayment = paymentRepository.findByBookingId(booking.getId());
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            if (newStatus == BookingStatus.HARD_BOOKED) {
                payment.setPaymentStatus(PaymentStatus.VERIFIED);
            } else if (newStatus == BookingStatus.CANCELLED) {
                payment.setPaymentStatus(PaymentStatus.REJECTED);
            }
            paymentRepository.save(payment);
        }

        return mapToResponse(updatedBooking);
    }

    private BookingResponse processBooking(BookingRequest request, String userEmail, BookingStatus status, MultipartFile proofImage) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceFacility service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (status == BookingStatus.PENDING_APPROVAL || status == BookingStatus.HARD_BOOKED) {
            validateDateAvailability(service, request.getStartDate(), request.getEndDate());
        }

        long daysBetween = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        BigDecimal totalPrice = service.getBasePrice().multiply(BigDecimal.valueOf(daysBetween));

        Booking booking = Booking.builder()
                .user(user)
                .service(service)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(status)
                .totalPrice(totalPrice)
                .notes(request.getNotes())
                .build();

        booking = bookingRepository.save(booking);

        if (status == BookingStatus.PENDING_APPROVAL && proofImage != null) {
            String imageUrl = fileStorageService.storeFile(proofImage);
            Payment payment = Payment.builder()
                    .booking(booking)
                    .proofImageUrl(imageUrl)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paidAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        }

        return mapToResponse(booking);
    }

    private void validateDateAvailability(ServiceFacility service, LocalDate startDate, LocalDate endDate) {
        List<BookingStatus> hardStatuses = Arrays.asList(
                BookingStatus.PENDING_APPROVAL,
                BookingStatus.HARD_BOOKED
        );

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(service.getId(), startDate, endDate, hardStatuses);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            long count = 0;
            for (Booking b : overlapping) {
                if (!date.isBefore(b.getStartDate()) && !date.isAfter(b.getEndDate())) {
                    count++;
                }
            }
            if (count >= service.getQuota()) {
                throw new IllegalStateException("The service is fully booked on " + date);
            }
        }
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .serviceName(booking.getService().getName())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .build();
    }
}