package com.bloodbank.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.management.entity.User;
import com.bloodbank.management.jwt.security.JwtUtils;
import com.bloodbank.management.requests.SignInRequest;
import com.bloodbank.management.requests.SignUpRequest;
import com.bloodbank.management.response.AuthResponseDTO;
import com.bloodbank.management.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody SignUpRequest signUpRequest) {
        AuthResponseDTO response = new AuthResponseDTO();

        // Validate fields
        if (signUpRequest.getFirstName() == null || signUpRequest.getFirstName().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("First name is required!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (signUpRequest.getLastName() == null || signUpRequest.getLastName().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Last name is required!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!isValidEmail(signUpRequest.getEmail())) {
            response.setSuccess(false);
            response.setMessage("Invalid email format!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if password meets the complexity requirements
        if (!isValidPassword(signUpRequest.getPassword())) {
            response.setSuccess(false);
            response.setMessage("Password must contain at least 8 characters with minimum 3 numbers and a special character!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if email is already registered
        if (userService.findByEmail(signUpRequest.getEmail()) != null) {
            response.setSuccess(false);
            response.setMessage("Email already registered!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Create a new user and save to the database
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("USER");

        User savedUser = userService.saveUser(user);

        response.setSuccess(true);
        response.setMessage("User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody SignInRequest signInRequest) {
        AuthResponseDTO response = new AuthResponseDTO();

        if (!isValidEmail(signInRequest.getEmail())) {
            response.setSuccess(false);
            response.setMessage("Invalid email format!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if password meets the complexity requirements
        if (!isValidPassword(signInRequest.getPassword())) {
            response.setSuccess(false);
            response.setMessage("Password must contain at least 8 characters with minimum 3 numbers and a special character!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByEmail(signInRequest.getEmail());

        if (user == null) {
            response.setSuccess(false);
            response.setMessage("Email not registered!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        // Get UserDetails from authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        response.setSuccess(true);
        response.setMessage("User Logged In!");
        response.setToken(jwtToken);
        response.setFirstName(user.getFirstName()); // Set the first name in response
        response.setLastName(user.getLastName()); // Set the last name in response
        response.setEmail(user.getEmail()); // Set the email in response
        response.setRole(user.getRole()); // Set the user role in response

        return ResponseEntity.ok(response);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

    private boolean isValidPassword(String password) {
        // At least 8 characters with minimum 3 numbers and a special character
        String regex = "^(?=.*[0-9]{3,})(?=.*[!@#$%^&*])(?=.*[a-zA-Z]).{8,}$";
        return password.matches(regex);
    }
}
