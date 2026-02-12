package com.pramaindia.customer_management.service.impl;

import com.pramaindia.customer_management.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary  // ← THIS IS IMPORTANT
@RequiredArgsConstructor
public class ProductionEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${app.from-email:reachme318151@gmail.com}")
    private String fromEmail;

    @Override
    public void sendResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request - Prama India Admin Portal");

            String resetLink = baseUrl + "/reset-password?token=" + resetToken;

            String emailText = String.format(
                    "Dear User,\n\n" +
                            "You have requested to reset your password.\n\n" +
                            "Reset Token: %s\n" +
                            "Reset Link: %s\n\n" +
                            "This link expires in 15 minutes.\n\n" +
                            "Best regards,\n" +
                            "Prama India Admin Portal Team",
                    resetToken, resetLink
            );

            message.setText(emailText);

            mailSender.send(message);

            log.info("✅ Email sent to: {}", toEmail);
            log.info("🔗 Reset link: {}", resetLink);

        } catch (Exception e) {
            log.error("❌ Failed to send email: {}", e.getMessage());
            // Fallback to console
            log.info("📋 Manual token for {}: {}", toEmail, resetToken);
        }
    }
}