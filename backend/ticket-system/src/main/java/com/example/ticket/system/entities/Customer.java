package com.example.ticket.system.entities;

import java.util.ArrayList;
import java.util.List;

import com.example.ticket.system.Role;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    public Customer() {
        super.setRole(Role.CUSTOMER);
    }

    public void createTicket(Ticket ticket) {
        // stub for creating a ticket; real implementation would persist via service/repository
    }

    public void replyToTicket(Ticket ticket, String message) {
        Message msg = new Message();
        msg.setSender(this);
        msg.setContent(message);
        msg.setTicket(ticket);
        ticket.getMessages().add(msg);
    }

    public Long getId() {
        return super.getId();
    }
}

