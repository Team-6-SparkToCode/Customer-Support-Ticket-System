package com.example.ticket.system.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Many-to-one relation to User (Staff or Customer)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name = "message", nullable = false, columnDefinition = "mediumtext")
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime timestamp; // DB default will populate

    // Optional convenience method to set sender and ticket together
    public void init(User sender, Ticket ticket, String content) {
        this.sender = sender;
        this.ticket = ticket;
        this.content = content;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setContent(String messageContent) {
        this.content = messageContent;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
