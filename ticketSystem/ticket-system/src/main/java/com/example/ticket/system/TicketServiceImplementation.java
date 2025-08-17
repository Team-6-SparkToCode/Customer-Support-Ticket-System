package com.example.ticket.system;

import com.example.ticket.system.entities.Message;
import com.example.ticket.system.entities.Staff;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImplementation implements TicketService {
    @Override
    public void assignTicket(Ticket ticket, Staff staff) {
        ticket.setAssignedStaff(staff);
    }

    @Override
    public void updateStatus(Ticket ticket, TicketStatus status) {
        ticket.setStatus(status);
    }

    @Override
    public void addMessage(Ticket ticket, User sender, String content) {
        Message msg = new Message();
        msg.setSender(sender);
        msg.setContent(content);
        msg.setTicket(ticket);
        ticket.getMessages().add(msg);
    }
}

