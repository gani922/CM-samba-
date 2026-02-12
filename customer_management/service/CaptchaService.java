package com.pramaindia.customer_management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CaptchaService {

    // In-memory store for captchas (use Redis in production)
    private final Map<String, String> captchaStore = new ConcurrentHashMap<>();
    private final Map<String, Integer> attemptCount = new ConcurrentHashMap<>();

    public String generateCaptcha() {
        String captcha = generateRandomCaptcha();
        String captchaId = System.currentTimeMillis() + "-" + Math.random();

        // Store captcha with 5-minute expiration
        captchaStore.put(captchaId, captcha);

        // Schedule cleanup (in production, use Redis TTL)
        scheduleCleanup(captchaId);

        return captchaId;
    }

    public String getCaptchaText(String captchaId) {
        return captchaStore.get(captchaId);
    }

    public boolean validate(String captchaId, String captchaToken) {
        if (captchaId == null || captchaToken == null) {
            return false;
        }

        // Check attempt count (prevent brute force)
        int attempts = attemptCount.getOrDefault(captchaId, 0);
        if (attempts >= 3) {
            log.warn("Too many captcha attempts for ID: {}", captchaId);
            captchaStore.remove(captchaId);
            attemptCount.remove(captchaId);
            return false;
        }

        String storedCaptcha = captchaStore.get(captchaId);
        if (storedCaptcha == null) {
            log.warn("Captcha not found or expired for ID: {}", captchaId);
            return false;
        }

        // Case-insensitive comparison
        boolean isValid = storedCaptcha.equalsIgnoreCase(captchaToken.trim());

        if (isValid) {
            // Remove captcha after successful validation
            captchaStore.remove(captchaId);
            attemptCount.remove(captchaId);
            return true;
        } else {
            // Increment attempt count
            attemptCount.put(captchaId, attempts + 1);
            return false;
        }
    }

    private String generateRandomCaptcha() {
        // Generate 6-character alphanumeric captcha
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return captcha.toString();
    }

    private void scheduleCleanup(String captchaId) {
        // In a real application, use Redis with TTL or a scheduled task
        new Thread(() -> {
            try {
                Thread.sleep(5 * 60 * 1000); // 5 minutes
                captchaStore.remove(captchaId);
                attemptCount.remove(captchaId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}