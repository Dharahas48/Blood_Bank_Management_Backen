package com.bloodbank.management.service;

import com.bloodbank.management.entity.User;

public interface UserService {
    User saveUser(User user);
    User findByEmail(String email);
}
