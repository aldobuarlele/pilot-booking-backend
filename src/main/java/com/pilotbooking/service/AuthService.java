package com.pilotbooking.service;

import com.pilotbooking.web.dto.request.AdminLoginRequest;
import com.pilotbooking.web.dto.request.OtpRequest;
import com.pilotbooking.web.dto.request.OtpVerificationRequest;
import com.pilotbooking.web.dto.response.AuthResponse;

public interface AuthService {
    void requestOtp(OtpRequest request);
    AuthResponse verifyOtp(OtpVerificationRequest request);
    AuthResponse loginAdmin(AdminLoginRequest request);
}