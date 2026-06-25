package com.example.demo.Controller;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Security.Jwtutils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final Jwtutils jwtutils;
    private final JavaMailSender mailSender;

    private int generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        log.info("Generated OTP: {}", otp);
        return otp;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {

        log.info("Register request received for email: {}", user.getEmail());

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Registration failed. Email already exists: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOtp(generateOtp());
        user.setVerified(false);

        log.info("Saving user: {}", user.getEmail());

        userRepo.save(user);

        try {
            sendOtpEmail(user.getEmail(), user.getOtp());
            log.info("OTP email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User created but email sending failed");
        }

        log.info("User registered successfully: {}", user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Registered successfully. Verify email.");
    }

    // ================= VERIFY =================
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email,
                                    @RequestParam int otp) {

        log.info("OTP verification request for email: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during verification: {}", email);
                    return new RuntimeException("User not found");
                });

        if (user.getVerified()) {
            log.warn("User already verified: {}", email);
            return ResponseEntity.badRequest().body("Already verified");
        }

        if (user.getOtp() != null && user.getOtp() == otp) {

            user.setVerified(true);
            user.setOtp(null);

            userRepo.save(user);

            log.info("OTP verified successfully for {}", email);

            return ResponseEntity.ok("Verified successfully");
        }

        log.warn("Invalid OTP entered for {}", email);

        return ResponseEntity.badRequest().body("Invalid OTP");
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body,
                                   HttpServletResponse response) {

        String email = body.get("email");

        log.info("Login request received for {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Login failed. User not found: {}", email);
                    return new RuntimeException("Invalid credentials");
                });

        if (!user.getVerified()) {
            log.warn("Login blocked. Email not verified: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email not verified");
        }

        if (!passwordEncoder.matches(body.get("password"), user.getPassword())) {
            log.warn("Invalid password attempt for {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        String token =
                jwtutils.genratetoken(user.getEmail(), user.getRole().name());

        log.info("JWT token generated for {}", email);

        jakarta.servlet.http.Cookie cookie =
                new jakarta.servlet.http.Cookie("token", token);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        log.info("Login successful for {}", email);

        return ResponseEntity.ok("Login successful");
    }

    // ================= ME =================
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpServletRequest request) {

        log.info("/me endpoint called");

        String email = (String) request.getAttribute("email");

        if (email == null) {
            log.warn("Unauthorized access to /me");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for /me endpoint: {}", email);
                    return new RuntimeException("User not found");
                });

        log.info("Returning profile details for {}", email);

        return ResponseEntity.ok(
                Map.of(
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "role", user.getRole().name()
                )
        );
    }

    // ================= SEND OTP =================
    private void sendOtpEmail(String to, int otp) {

        log.info("Preparing OTP email for {}", to);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("OTP Verification");
        msg.setText("Your OTP is: " + otp);

        mailSender.send(msg);

        log.info("OTP email sent successfully to {}", to);
    }


}
