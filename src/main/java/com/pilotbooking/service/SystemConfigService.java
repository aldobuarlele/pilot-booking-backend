package com.pilotbooking.service;

import com.pilotbooking.web.dto.request.SystemConfigRequest;
import com.pilotbooking.web.dto.response.SystemConfigResponse;

import java.util.List;

public interface SystemConfigService {
    List<SystemConfigResponse> getAllConfigs();
    SystemConfigResponse saveOrUpdateConfig(SystemConfigRequest request);
}