package com.pilotbooking.web.controller;

import com.pilotbooking.service.BookingService;
import com.pilotbooking.web.dto.request.UpdateBookingStatusRequest;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> data = bookingService.getAllBookingsForAdmin();
        return ResponseEntity.ok(BaseResponse.<List<BookingResponse>>builder()
                .status(200)
                .message("Success fetching all bookings")
                .data(data)
                .build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BaseResponse<BookingResponse>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookingStatusRequest request) {
        BookingResponse data = bookingService.updateBookingStatus(id, request.getStatus());
        return ResponseEntity.ok(BaseResponse.<BookingResponse>builder()
                .status(200)
                .message("Booking status updated successfully")
                .data(data)
                .build());
    }
}