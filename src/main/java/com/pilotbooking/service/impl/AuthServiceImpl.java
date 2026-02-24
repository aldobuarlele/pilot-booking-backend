package com.pilotbooking.service.impl;

import com.pilotbooking.domain.User;
import com.pilotbooking.repository.UserRepository;
import com.pilotbooking.security.JwtUtil;
import com.pilotbooking.service.AuthService;
import com.pilotbooking.service.OtpService;
import com.pilotbooking.web.dto.request.AdminLoginRequest;
import com.pilotbooking.web.dto.request.OtpRequest;
import com.pilotbooking.web.dto.request.OtpVerificationRequest;
import com.pilotbooking.web.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public void requestOtp(OtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> User.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .phone(request.getPhone())
                        .build());
        userRepository.save(user);
        otpService.generateAndStoreOtp(request.getEmail());
    }

    @Override
    @Transactional
    public AuthResponse verifyOtp(OtpVerificationRequest request) {
        boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());
        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsVerified(true);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .role("USER")
                .build();
    }

    @Override
    public AuthResponse loginAdmin(AdminLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .role("ADMIN")
                .build();
    }
}