package com.example.ticket.system.dto;
import com.example.ticket.system.Role;

public class LoginResponse {
    private String token;
    private String name;
    private String email;
    private Role role;

    public LoginResponse(String token, String name, Role role, String email) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
