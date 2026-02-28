package com.pilotbooking.service;

import com.pilotbooking.web.dto.response.DashboardMetricsResponse;

public interface DashboardService {
    DashboardMetricsResponse getMetrics();
}