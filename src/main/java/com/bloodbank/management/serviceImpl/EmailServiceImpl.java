package com.bloodbank.management.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bloodbank.management.entity.ResetTokens;
import com.bloodbank.management.repository.ResetTokensRepository;
import com.bloodbank.management.repository.UserRepository;
import com.bloodbank.management.requests.ResetPasswordReqDTO;
import com.bloodbank.management.service.EmailService;
import com.bloodbank.management.entity.*;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokensRepository resetTokensRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${frontend.reset.url}")
    private String frontendResetUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public ResponseEntity<String> sendPasswordResetEmail(String email) {
        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                // Generate a temporary reset token or link
                String token = UUID.randomUUID().toString();

                // Create a ResetTokens object and save it
                ResetTokens resetTokens = new ResetTokens();
                resetTokens.setResetToken(token);
                resetTokens.setCreationDate(new Date(System.currentTimeMillis()));
                resetTokens.setUser(user);
                resetTokensRepository.save(resetTokens);

                // Construct the reset password link with the token
                String resetLink = frontendResetUrl + token;

                // Create a SimpleMailMessage
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Password Reset");
                message.setText("Please click the following link to reset your password:\n" + resetLink);

                // Send the email
                emailSender.send(message);

                // Return success response
                return ResponseEntity.ok("Password reset email sent successfully!");
            } catch (Exception e) {
                // Log the error
                LOGGER.error("Failed to send password reset email: {}", e.getMessage());

                // Return error response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send password reset email.");
            }
        } else {
            // User not found with the provided email address
            return ResponseEntity.badRequest().body("User not found with the provided email address.");
        }
    }

    @Override
    public ResponseEntity<String> passwordReset(ResetPasswordReqDTO req) {
        // Find the reset token by token string
        Optional<ResetTokens> resetTokenOptional = resetTokensRepository.findByResetToken(req.getToken());

        if (resetTokenOptional.isPresent()) {
            ResetTokens resetTokens = resetTokenOptional.get();
            // Check if token has not expired (24 hours)
            if (resetTokens.getCreationDate().getTime() + (24 * 60 * 60 * 1000) > System.currentTimeMillis()) {
                User user = resetTokens.getUser();
                // Encode the new password
                user.setPassword(passwordEncoder.encode(req.getNewPassword()));

                // Save the updated user with new password
                userRepository.save(user);

                // Delete the reset token
                resetTokensRepository.delete(resetTokens);

                return ResponseEntity.ok("Password reset successfully!");
            } else {
                // Token has expired
                resetTokensRepository.delete(resetTokens);
                return ResponseEntity.badRequest().body("Reset token has expired.");
            }
        } else {
            // Invalid token
            return ResponseEntity.badRequest().body("Invalid reset token.");
        }
    }

}
