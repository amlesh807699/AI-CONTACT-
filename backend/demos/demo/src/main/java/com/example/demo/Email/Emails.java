package com.example.demo.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Emails {

    private final JavaMailSender mailSender;

    @Autowired
    public Emails(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send a simple email with subject and text
     * @param to Recipient email
     * @param subject Email subject
     * @param text Email body
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    /**
     * Send OTP email
     * @param to Recipient email
     * @param otp OTP code
     */
    public void sendOtp(String to, int otp) {
        String subject = "Your OTP Code";
        String text = "Your OTP is: " + otp + "\nIt is valid for 10 minutes.";
        sendEmail(to, subject, text);
    }
}
