package com.pilotbooking.web.controller;

import com.pilotbooking.service.BookingService;
import com.pilotbooking.service.ServiceFacilityService;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final ServiceFacilityService serviceFacilityService;
    private final BookingService bookingService;

    @GetMapping("/services")
    public ResponseEntity<BaseResponse<List<ServiceFacilityResponse>>> getServices() {
        List<ServiceFacilityResponse> data = serviceFacilityService.getAllActiveServices();
        return ResponseEntity.ok(BaseResponse.<List<ServiceFacilityResponse>>builder()
                .status(200)
                .message("Success fetching services")
                .data(data)
                .build());
    }

    @GetMapping("/services/{id}/unavailable-dates")
    public ResponseEntity<BaseResponse<List<Map<String, LocalDate>>>> getUnavailableDates(@PathVariable UUID id) {
        List<Map<String, LocalDate>> data = bookingService.getUnavailableDates(id);
        return ResponseEntity.ok(BaseResponse.<List<Map<String, LocalDate>>>builder()
                .status(200)
                .message("Success fetching unavailable dates")
                .data(data)
                .build());
    }
}