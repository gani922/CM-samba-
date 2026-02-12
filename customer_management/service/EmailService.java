package com.pramaindia.customer_management.service;

public interface EmailService {
    void sendResetEmail(String toEmail, String resetToken);
}