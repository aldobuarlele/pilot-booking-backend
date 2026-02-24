package com.pilotbooking.web.controller;

import com.pilotbooking.service.AuthService;
import com.pilotbooking.web.dto.request.AdminLoginRequest;
import com.pilotbooking.web.dto.request.OtpRequest;
import com.pilotbooking.web.dto.request.OtpVerificationRequest;
import com.pilotbooking.web.dto.response.AuthResponse;
import com.pilotbooking.web.dto.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/otp/request")
    public ResponseEntity<BaseResponse<Void>> requestOtp(@Valid @RequestBody OtpRequest request) {
        authService.requestOtp(request);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .status(200)
                .message("OTP sent successfully")
                .build());
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<BaseResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(BaseResponse.<AuthResponse>builder()
                .status(200)
                .message("OTP verified successfully")
                .data(response)
                .build());
    }

    @PostMapping("/admin/login")
    public ResponseEntity<BaseResponse<AuthResponse>> loginAdmin(@Valid @RequestBody AdminLoginRequest request) {
        AuthResponse response = authService.loginAdmin(request);
        return ResponseEntity.ok(BaseResponse.<AuthResponse>builder()
                .status(200)
                .message("Admin logged in successfully")
                .data(response)
                .build());
    }
}