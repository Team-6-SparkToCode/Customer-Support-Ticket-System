package com.SparkSupport.service;

import com.SparkSupport.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final ConcurrentHashMap<String, User> userDB = new ConcurrentHashMap<>();

    public void save(User user) {
        userDB.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return userDB.get(email);
    }

    public boolean validateCredentials(String email, String password) {
        User user = userDB.get(email);
        return user != null && user.getPassword().equals(password);
    }
}
