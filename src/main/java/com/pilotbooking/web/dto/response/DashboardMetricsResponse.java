package com.pilotbooking.web.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DashboardMetricsResponse {
    private long totalActiveServices;
    private long totalPendingApprovals;
    private long totalSoftBookings;
    private long totalCompletedBookings;
    private BigDecimal totalRevenue;
}