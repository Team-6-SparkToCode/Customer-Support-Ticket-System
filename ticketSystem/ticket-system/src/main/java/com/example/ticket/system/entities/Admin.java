package com.example.ticket.system.entities;

import com.example.ticket.system.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Staff {
    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    // Admin capabilities - stubs for now
    public void createStaff(User userData) {}
    public void updateStaff(User staff) {}
    public void deleteStaff(Long staffId) {}
    public void manageCategory(Category category) {}
    public void managePriority(Priority priority) {}
}

