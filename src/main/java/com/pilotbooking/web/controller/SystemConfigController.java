package com.pilotbooking.web.controller;

import com.pilotbooking.service.SystemConfigService;
import com.pilotbooking.web.dto.request.SystemConfigRequest;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.SystemConfigResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @GetMapping("/public/configs")
    public ResponseEntity<BaseResponse<List<SystemConfigResponse>>> getPublicConfigs() {
        List<SystemConfigResponse> data = systemConfigService.getAllConfigs();
        return ResponseEntity.ok(BaseResponse.<List<SystemConfigResponse>>builder()
                .status(200)
                .message("Success fetching configs")
                .data(data)
                .build());
    }

    @PostMapping("/admin/configs")
    public ResponseEntity<BaseResponse<SystemConfigResponse>> saveConfig(
            @Valid @RequestBody SystemConfigRequest request) {
        SystemConfigResponse data = systemConfigService.saveOrUpdateConfig(request);
        return ResponseEntity.ok(BaseResponse.<SystemConfigResponse>builder()
                .status(200)
                .message("Config saved successfully")
                .data(data)
                .build());
    }
}