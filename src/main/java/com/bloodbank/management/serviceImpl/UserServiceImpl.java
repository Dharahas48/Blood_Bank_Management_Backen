package com.bloodbank.management.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.management.entity.User;
import com.bloodbank.management.repository.UserRepository;
import com.bloodbank.management.service.UserService;

import java.util.Optional; // Import Optional class

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        user.setRole("USER");  // Set default role
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // Return null if userOptional is empty
    }
}
