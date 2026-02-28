package com.pilotbooking.web.controller;

import com.pilotbooking.service.ServiceFacilityService;
import com.pilotbooking.web.dto.request.ServiceFacilityRequest;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/services")
@RequiredArgsConstructor
public class AdminController {

    private final ServiceFacilityService serviceFacilityService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ServiceFacilityResponse>>> getAllServices() {
        List<ServiceFacilityResponse> data = serviceFacilityService.getAllServicesForAdmin();
        return ResponseEntity.ok(BaseResponse.<List<ServiceFacilityResponse>>builder()
                .status(200)
                .message("Success fetching all services")
                .data(data)
                .build());
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<BaseResponse<ServiceFacilityResponse>> createService(
            @Valid @RequestPart("data") ServiceFacilityRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ServiceFacilityResponse data = serviceFacilityService.createService(request, image);

        return ResponseEntity.ok(BaseResponse.<ServiceFacilityResponse>builder()
                .status(200)
                .message("Service created successfully")
                .data(data)
                .build());
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<BaseResponse<ServiceFacilityResponse>> updateService(
            @PathVariable UUID id,
            @Valid @RequestPart("data") ServiceFacilityRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ServiceFacilityResponse data = serviceFacilityService.updateService(id, request, image);

        return ResponseEntity.ok(BaseResponse.<ServiceFacilityResponse>builder()
                .status(200)
                .message("Service updated successfully")
                .data(data)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteService(@PathVariable UUID id) {
        serviceFacilityService.deleteService(id);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .status(200)
                .message("Service deleted successfully")
                .build());
    }
}