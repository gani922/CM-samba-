package com.pramaindia.customer_management.service.impl;

import com.pramaindia.customer_management.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")  // ← Only active in dev profile (NOT @Primary)
public class DevelopmentEmailService implements EmailService {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public void sendResetEmail(String toEmail, String resetToken) {
        String resetLink = baseUrl + "/reset-password?token=" + resetToken;

        System.out.println("\n" + "=".repeat(70));
        System.out.println("   📧 DEVELOPMENT MODE - NO REAL EMAIL SENT   ");
        System.out.println("=".repeat(70));
        System.out.println("👤 To: " + toEmail);
        System.out.println("🔑 Token: " + resetToken);
        System.out.println("🔗 Link: " + resetLink);
        System.out.println("=".repeat(70) + "\n");
    }
}