package com.pilotbooking.service.impl;

import com.pilotbooking.domain.SystemConfig;
import com.pilotbooking.repository.SystemConfigRepository;
import com.pilotbooking.service.SystemConfigService;
import com.pilotbooking.web.dto.request.SystemConfigRequest;
import com.pilotbooking.web.dto.response.SystemConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository repository;

    @Override
    public List<SystemConfigResponse> getAllConfigs() {
        return repository.findAll().stream()
                .map(config -> SystemConfigResponse.builder()
                        .key(config.getKey())
                        .value(config.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SystemConfigResponse saveOrUpdateConfig(SystemConfigRequest request) {
        SystemConfig config = repository.findById(request.getKey())
                .orElse(SystemConfig.builder().key(request.getKey()).build());

        config.setValue(request.getValue());
        SystemConfig saved = repository.save(config);

        return SystemConfigResponse.builder()
                .key(saved.getKey())
                .value(saved.getValue())
                .build();
    }
}