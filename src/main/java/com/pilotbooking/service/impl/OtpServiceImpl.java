package com.pilotbooking.service.impl;

import com.pilotbooking.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final String OTP_PREFIX = "OTP:";
    private static final long OTP_VALIDITY_MINUTES = 5;

    @Override
    public String generateAndStoreOtp(String identifier) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        redisTemplate.opsForValue().set(OTP_PREFIX + identifier, otp, Duration.ofMinutes(OTP_VALIDITY_MINUTES));
        log.info("Generated OTP for {}: {}", identifier, otp);
        return otp;
    }

    @Override
    public boolean validateOtp(String identifier, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + identifier);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(OTP_PREFIX + identifier);
            return true;
        }
        return false;
    }
}