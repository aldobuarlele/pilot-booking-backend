package com.pilotbooking.web.controller;

import com.pilotbooking.service.DashboardService;
import com.pilotbooking.web.dto.response.BaseResponse;
import com.pilotbooking.web.dto.response.DashboardMetricsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<BaseResponse<DashboardMetricsResponse>> getMetrics() {
        DashboardMetricsResponse metrics = dashboardService.getMetrics();

        BaseResponse<DashboardMetricsResponse> response = BaseResponse.<DashboardMetricsResponse>builder()
                .message("Dashboard metrics retrieved successfully")
                .data(metrics)
                .build();

        return ResponseEntity.ok(response);
    }
}