package com.pilotbooking.web.controller;

import com.pilotbooking.web.dto.request.BookingRequest;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.BookingResponse;
import com.pilotbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/soft")
    public ResponseEntity<BaseResponse<BookingResponse>> createSoftBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        BookingResponse response = bookingService.createSoftBooking(request, userEmail);
        return ResponseEntity.ok(BaseResponse.<BookingResponse>builder()
                .status(200)
                .message("Soft booking created successfully")
                .data(response)
                .build());
    }

    @PostMapping(value = "/hard", consumes = { "multipart/form-data" })
    public ResponseEntity<BaseResponse<BookingResponse>> createHardBooking(
            @Valid @RequestPart("data") BookingRequest request,
            @RequestPart("image") MultipartFile image,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        BookingResponse response = bookingService.createHardBooking(request, image, userEmail);
        return ResponseEntity.ok(BaseResponse.<BookingResponse>builder()
                .status(200)
                .message("Hard booking pending approval")
                .data(response)
                .build());
    }
}