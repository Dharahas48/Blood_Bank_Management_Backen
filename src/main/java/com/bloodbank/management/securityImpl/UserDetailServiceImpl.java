package com.bloodbank.management.securityImpl;

import com.bloodbank.management.entity.User;
import com.bloodbank.management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        
        // Check if user with given email exists
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with email: " + username);
        }

        User user = userOptional.get();
        return UserDetailsImpl.build(user);
    }
}
