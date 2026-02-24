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

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceFacilityRepository serviceRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

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
        List<BookingStatus> activeStatuses = Arrays.asList(
                BookingStatus.SOFT_BOOKED,
                BookingStatus.PENDING_APPROVAL,
                BookingStatus.HARD_BOOKED
        );

        List<Booking> bookings = bookingRepository.findActiveBookingsForService(serviceId, activeStatuses, LocalDate.now());

        List<Map<String, LocalDate>> unavailableDates = new ArrayList<>();
        for (Booking b : bookings) {
            Map<String, LocalDate> dateRange = new HashMap<>();
            dateRange.put("start", b.getStartDate());
            dateRange.put("end", b.getEndDate());
            unavailableDates.add(dateRange);
        }
        return unavailableDates;
    }

    private BookingResponse processBooking(BookingRequest request, String userEmail, BookingStatus status, MultipartFile proofImage) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceFacility service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        validateDateAvailability(service.getId(), request.getStartDate(), request.getEndDate());

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
            String imageUrl = uploadImageMock(proofImage);
            Payment payment = Payment.builder()
                    .booking(booking)
                    .proofImageUrl(imageUrl)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paidAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        }

        return BookingResponse.builder()
                .id(booking.getId())
                .serviceName(service.getName())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .build();
    }

    private void validateDateAvailability(UUID serviceId, LocalDate startDate, LocalDate endDate) {
        List<BookingStatus> activeStatuses = Arrays.asList(
                BookingStatus.SOFT_BOOKED,
                BookingStatus.PENDING_APPROVAL,
                BookingStatus.HARD_BOOKED
        );
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(serviceId, startDate, endDate, activeStatuses);
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("The selected dates are already booked.");
        }
    }

    private String uploadImageMock(MultipartFile file) {
        return "/uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    }
}