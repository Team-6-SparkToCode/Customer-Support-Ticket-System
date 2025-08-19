package com.example.ticket.system;

import com.example.ticket.system.entities.Staff;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;

public interface TicketService {
    void assignTicket(Ticket ticket, Staff staff);
    void updateStatus(Ticket ticket, TicketStatus status);
    void addMessage(Ticket ticket, User sender, String content);
}

