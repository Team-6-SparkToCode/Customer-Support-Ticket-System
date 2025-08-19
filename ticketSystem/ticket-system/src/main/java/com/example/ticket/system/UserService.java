package com.example.ticket.system;

import com.example.ticket.system.entities.User;

public interface UserService {
    void promoteToStaff(User user);
    void demoteToCustomer(User user);
}

