package com.example.ticket.system.controller;

import com.example.ticket.system.entities.User;
import com.example.ticket.system.service.UserService;
import com.example.ticket.system.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        User u = userService.getUserByEmail(req.getEmail());
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(u.getEmail(), u.getRoleEnum().name());
        return Map.of("token", token, "role", u.getRoleEnum().name(), "email", u.getEmail());
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {
        // Stateless: instruct client to drop token. For server-enforced logout implement blacklist.
        return Map.of("message", "ok");
    }

    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String e) { this.email = e; }
        public String getPassword() { return password; }
        public void setPassword(String p) { this.password = p; }
    }
}