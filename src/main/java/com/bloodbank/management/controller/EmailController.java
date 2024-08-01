package com.bloodbank.management.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.management.entity.ResetTokens;
import com.bloodbank.management.repository.ResetTokensRepository;
import com.bloodbank.management.repository.UserRepository;
import com.bloodbank.management.requests.EmailRequest;
import com.bloodbank.management.requests.ResetPasswordReqDTO;
import com.bloodbank.management.service.EmailService;
import com.bloodbank.management.entity.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokensRepository resetTokensRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-password/reset-email")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestBody EmailRequest request) {
        return emailService.sendPasswordResetEmail(request.getEmail());
    }

    @PostMapping("/reset/password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordReqDTO req) {
        // Find the reset token by token string
        Optional<ResetTokens> optionalResetToken = resetTokensRepository.findByResetToken(req.getToken());

        if (optionalResetToken.isPresent()) {
            ResetTokens resetToken = optionalResetToken.get();
            User user = resetToken.getUser();

            if (user != null) {
                // Encode the new password
                user.setPassword(passwordEncoder.encode(req.getNewPassword()));

                // Delete the reset token after password reset
                resetTokensRepository.delete(resetToken);

                // Save the updated user with new password
                userRepository.save(user);

                return ResponseEntity.ok("Password reset successfully!");
            } else {
                // Handle the case where user associated with reset token is null
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found for reset token.");
            }
        } else {
            // Handle the case where reset token is not found
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
}
