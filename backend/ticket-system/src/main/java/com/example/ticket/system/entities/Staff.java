package com.example.ticket.system.entities;

import com.example.ticket.system.Role;
import com.example.ticket.system.TicketStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("STAFF")
public class Staff extends User {
    @OneToMany(mappedBy = "assignedStaff", fetch = FetchType.LAZY)
    private List<Ticket> assignedTickets = new ArrayList<>();

    public Staff() {
        super.setRole(Role.STAFF);
    }

    public void assignTicket(Ticket ticket, Staff staff) {
        ticket.setAssignedStaff(staff);
    }

    public void updateTicketStatus(Ticket ticket, TicketStatus status) {
        ticket.setStatus(status);
    }

    public void replyToTicket(Ticket ticket, String messageContent) {
        Message msg = new Message();
        msg.setSender(this);
        msg.setContent(messageContent);
        msg.setTicket(ticket);
        ticket.getMessages().add(msg);
    }

    public void addInternalNote(Ticket ticket, String note) {
        ticket.addInternalNote(note);
    }
}

