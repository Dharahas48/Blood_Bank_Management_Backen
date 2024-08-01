package com.bloodbank.management.service;



import org.springframework.http.ResponseEntity;

import com.bloodbank.management.requests.ResetPasswordReqDTO;

public interface EmailService {

    ResponseEntity<String> sendPasswordResetEmail(String email);

    ResponseEntity<String> passwordReset(ResetPasswordReqDTO req);
}
