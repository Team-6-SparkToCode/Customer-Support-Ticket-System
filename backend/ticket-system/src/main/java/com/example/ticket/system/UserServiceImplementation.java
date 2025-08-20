package com.example.ticket.system;

import com.example.ticket.system.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    @Override
    public void promoteToStaff(User user) {
        System.out.println(user.getName() + " promoted to STAFF (placeholder)");
    }

    @Override
    public void demoteToCustomer(User user) {
        System.out.println(user.getName() + " demoted to CUSTOMER (placeholder)");
    }
}
