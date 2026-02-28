package com.pilotbooking.service;

import com.pilotbooking.web.dto.request.ServiceFacilityRequest;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ServiceFacilityService {
    List<ServiceFacilityResponse> getAllActiveServices();
    List<ServiceFacilityResponse> getAllServicesForAdmin();
    ServiceFacilityResponse createService(ServiceFacilityRequest request, MultipartFile image);
    ServiceFacilityResponse updateService(UUID id, ServiceFacilityRequest request, MultipartFile image);
    void deleteService(UUID id);
}