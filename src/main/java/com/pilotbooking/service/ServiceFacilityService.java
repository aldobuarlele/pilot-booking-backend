package com.pilotbooking.service;

import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import java.util.List;

public interface ServiceFacilityService {
    List<ServiceFacilityResponse> getAllActiveServices();
}