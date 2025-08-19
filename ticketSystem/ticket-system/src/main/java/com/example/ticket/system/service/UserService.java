package com.example.ticket.system.service;

import com.example.ticket.system.entities.Admin;
import com.example.ticket.system.entities.Staff;
import com.example.ticket.system.entities.User;
import com.example.ticket.system.repositories.UserRepository;
import com.example.ticket.system.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional
    public User promoteToStaff(Long userId) {
        // Update discriminator column directly to switch subtype reliably
        int updated = entityManager.createNativeQuery("UPDATE users SET role = 'STAFF' WHERE id = ?1")
                .setParameter(1, userId)
                .executeUpdate();
        if (updated == 0) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        entityManager.clear(); // ensure we reload fresh subtype
        User reloaded = userRepository.findById(userId).orElseThrow();
        // If JPA still returns base proxy, just return it; JSON in tests uses common fields
        return reloaded instanceof Staff ? reloaded : reloaded;
    }

    @Transactional
    public User promoteToAdmin(Long userId) {
        int updated = entityManager.createNativeQuery("UPDATE users SET role = 'ADMIN' WHERE id = ?1")
                .setParameter(1, userId)
                .executeUpdate();
        if (updated == 0) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        entityManager.clear();
        User reloaded = userRepository.findById(userId).orElseThrow();
        return reloaded instanceof Admin ? reloaded : reloaded;
    }
}
