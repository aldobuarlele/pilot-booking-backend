package com.pilotbooking.service.impl;

import com.pilotbooking.domain.enums.BookingStatus;
import com.pilotbooking.repository.BookingRepository;
import com.pilotbooking.repository.ServiceFacilityRepository;
import com.pilotbooking.service.DashboardService;
import com.pilotbooking.web.dto.response.DashboardMetricsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final ServiceFacilityRepository serviceFacilityRepository;

    @Override
    public DashboardMetricsResponse getMetrics() {
        long activeServices = serviceFacilityRepository.countByIsActiveTrue();
        long pendingApprovals = bookingRepository.countByStatus(BookingStatus.PENDING_APPROVAL);
        long softBookings = bookingRepository.countByStatus(BookingStatus.SOFT_BOOKED);
        long completedBookings = bookingRepository.countByStatus(BookingStatus.HARD_BOOKED);

        BigDecimal totalRevenue = bookingRepository.sumTotalPriceByStatus(BookingStatus.HARD_BOOKED);

        return DashboardMetricsResponse.builder()
                .totalActiveServices(activeServices)
                .totalPendingApprovals(pendingApprovals)
                .totalSoftBookings(softBookings)
                .totalCompletedBookings(completedBookings)
                .totalRevenue(totalRevenue)
                .build();
    }
}