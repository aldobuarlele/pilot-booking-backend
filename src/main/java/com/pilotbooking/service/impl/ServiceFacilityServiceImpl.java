package com.pilotbooking.service.impl;

import com.pilotbooking.domain.ServiceFacility;
import com.pilotbooking.repository.ServiceFacilityRepository;
import com.pilotbooking.service.ServiceFacilityService;
import com.pilotbooking.web.dto.request.ServiceFacilityRequest;
import com.pilotbooking.web.dto.response.ServiceFacilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceFacilityServiceImpl implements ServiceFacilityService {

    private final ServiceFacilityRepository repository;

    @Override
    public List<ServiceFacilityResponse> getAllActiveServices() {
        return repository.findByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceFacilityResponse> getAllServicesForAdmin() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceFacilityResponse createService(ServiceFacilityRequest request) {
        ServiceFacility service = ServiceFacility.builder()
                .name(request.getName())
                .description(request.getDescription())
                .basePrice(request.getBasePrice())
                .imageUrl(request.getImageUrl())
                .quota(request.getQuota())
                .isActive(request.getIsActive())
                .build();

        ServiceFacility saved = repository.save(service);
        return mapToResponse(saved);
    }

    @Override
    public ServiceFacilityResponse updateService(UUID id, ServiceFacilityRequest request) {
        ServiceFacility service = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        service.setImageUrl(request.getImageUrl());
        service.setQuota(request.getQuota());
        service.setIsActive(request.getIsActive());

//        updateIfPresent(request.getName(), service::setName);
//        updateIfPresent(request.getDescription(), service::setDescription);
//        updateIfPresent(request.getBasePrice(), service::setBasePrice);
//        updateIfPresent(request.getImageUrl(), service::setImageUrl);
//        updateIfPresent(request.getQuota(), service::setQuota);
//        updateIfPresent(request.getIsActive(), service::setIsActive);

        ServiceFacility updated = repository.save(service);
        return mapToResponse(updated);
    }

    @Override
    public void deleteService(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Service not found");
        }
        repository.deleteById(id);
    }

    private ServiceFacilityResponse mapToResponse(ServiceFacility service) {
        return ServiceFacilityResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .basePrice(service.getBasePrice())
                .imageUrl(service.getImageUrl())
                .quota(service.getQuota())
                .isActive(service.getIsActive())
                .build();
    }

    private <T> void updateIfPresent(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }
}