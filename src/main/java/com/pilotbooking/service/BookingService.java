package com.pilotbooking.service;

import com.pilotbooking.domain.enums.BookingStatus;
import com.pilotbooking.web.dto.request.BookingRequest;
import com.pilotbooking.web.dto.response.BookingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingService {
    BookingResponse createSoftBooking(BookingRequest request, String userEmail);
    BookingResponse createHardBooking(BookingRequest request, MultipartFile proofImage, String userEmail);
    List<Map<String, LocalDate>> getUnavailableDates(UUID serviceId);
    List<BookingResponse> getAllBookingsForAdmin();
    BookingResponse updateBookingStatus(UUID bookingId, BookingStatus newStatus);
}