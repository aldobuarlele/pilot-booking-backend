package com.pilotbooking.service.impl;

import com.pilotbooking.repository.ServiceFacilityRepository;
import com.pilotbooking.service.ServiceFacilityService;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceFacilityServiceImpl implements ServiceFacilityService {

    private final ServiceFacilityRepository repository;

    @Override
    public List<ServiceFacilityResponse> getAllActiveServices() {
        return repository.findByIsActiveTrue().stream()
                .map(service -> ServiceFacilityResponse.builder()
                        .id(service.getId())
                        .name(service.getName())
                        .description(service.getDescription())
                        .basePrice(service.getBasePrice())
                        .imageUrl(service.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}