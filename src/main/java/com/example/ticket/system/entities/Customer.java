package com.example.ticket.system.entities;

import com.example.ticket.system.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public Role getRole() {
        return Role.CUSTOMER;
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
}

