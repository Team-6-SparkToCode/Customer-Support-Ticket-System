package com.SparkSupport.controller;

import com.SparkSupport.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    // === Registration ===
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        // TODO: Save user to database (if using DB layer)
        // e.g., userService.save(user);

        model.addAttribute("message", "Account created successfully!");
        return "redirect:/login";
    }

    // === Login ===
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model) {
        // TODO: Implement actual authentication check
        if (email.equals("test@example.com") && password.equals("password")) {
            model.addAttribute("message", "Logged in successfully!");
            return "redirect:/dashboard"; // Update if you create a dashboard page
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    // === Forgot Password ===
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        // TODO: Send password reset email logic
        model.addAttribute("message", "Password reset link sent to your email.");
        return "forgot_password";
    }
}
