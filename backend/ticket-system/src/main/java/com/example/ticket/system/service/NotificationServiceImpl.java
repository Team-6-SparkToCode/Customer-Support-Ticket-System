package com.example.ticket.system.service;

import com.example.ticket.system.NotificationType;
import com.example.ticket.system.entities.Notification;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;
import com.example.ticket.system.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification notify(User user, Ticket ticket, NotificationType type, String message) {
        if (user == null || type == null || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Notification requires user, type, and non-empty message");
        }
        Notification n = new Notification();
        n.setUser(user);
        n.setTicket(ticket);
        n.setType(type);
        n.setMessage(message);
        n.setSentAt(LocalDateTime.now());
        return notificationRepository.save(n);
    }
}
