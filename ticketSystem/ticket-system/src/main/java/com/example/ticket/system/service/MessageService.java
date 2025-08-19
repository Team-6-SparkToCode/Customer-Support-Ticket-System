package com.example.ticket.system.service;

import com.example.ticket.system.entities.*;
import com.example.ticket.system.repositories.MessageRepository;
import com.example.ticket.system.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public Message replyToTicket(Long ticketId, User sender, String content) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();

        Message msg = new Message();
        msg.setTicket(ticket);
        msg.setSender(sender);
        msg.setContent(content);

        return messageRepository.save(msg);
    }

    public List<Message> getMessagesForTicket(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }
}
