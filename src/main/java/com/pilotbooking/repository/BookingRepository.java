package com.pilotbooking.repository;

import com.pilotbooking.domain.Booking;
import com.pilotbooking.domain.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.service.id = :serviceId AND b.status IN :statuses AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
    List<Booking> findOverlappingBookings(
            @Param("serviceId") UUID serviceId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("statuses") List<BookingStatus> statuses
    );

    @Query("SELECT b FROM Booking b WHERE b.service.id = :serviceId AND b.status IN :statuses AND b.endDate >= :today")
    List<Booking> findActiveBookingsForService(
            @Param("serviceId") UUID serviceId,
            @Param("statuses") List<BookingStatus> statuses,
            @Param("today") LocalDate today
    );
}