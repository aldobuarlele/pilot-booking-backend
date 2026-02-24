package com.pilotbooking.web.dto.request;

import com.pilotbooking.domain.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusRequest {
    @NotNull
    private BookingStatus status;
}