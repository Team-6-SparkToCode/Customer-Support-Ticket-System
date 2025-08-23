package com.example.ticket.system.repositories;

import com.example.ticket.system.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderBySentAtDesc(Long userId);
    List<Notification> findByTicketId(Long ticketId);
    List<Notification> findByUserId(Long userId);
}
