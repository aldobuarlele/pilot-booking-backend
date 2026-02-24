package com.pilotbooking.service;

import com.pilotbooking.web.dto.request.ServiceFacilityRequest;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import java.util.List;
import java.util.UUID;

public interface ServiceFacilityService {
    List<ServiceFacilityResponse> getAllActiveServices();
    List<ServiceFacilityResponse> getAllServicesForAdmin();
    ServiceFacilityResponse createService(ServiceFacilityRequest request);
    ServiceFacilityResponse updateService(UUID id, ServiceFacilityRequest request);
    void deleteService(UUID id);
}