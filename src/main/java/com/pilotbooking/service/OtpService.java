package com.pilotbooking.service;

public interface OtpService {
    String generateAndStoreOtp(String identifier);
    boolean validateOtp(String identifier, String otp);
}