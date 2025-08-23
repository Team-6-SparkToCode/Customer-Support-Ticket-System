package com.example.ticket.system.service;

import com.example.ticket.system.NotificationType;
import com.example.ticket.system.entities.Notification;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;

public interface NotificationService {
    Notification notify(User user, Ticket ticket, NotificationType type, String message);

    default Notification notify(User user, NotificationType type, String message) {
        return notify(user, null, type, message);
    }
}
