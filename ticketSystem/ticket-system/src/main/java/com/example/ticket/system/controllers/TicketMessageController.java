package com.example.ticket.system.controllers;

import com.example.ticket.system.Role;
import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Message;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;
import com.example.ticket.system.repositories.MessageRepository;
import com.example.ticket.system.repositories.TicketRepository;
import com.example.ticket.system.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketMessageController {

    private final MessageRepository messageRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketMessageController(MessageRepository messageRepository,
                                   TicketRepository ticketRepository,
                                   UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/tickets/{ticketId}/messages")
    public List<Message> getMessages(@PathVariable Long ticketId,
                                     @RequestParam("userId") Long userId) {
        // 1) Load current user + ticket
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        // 2) Permission: admin OR ticket owner OR assigned staff
        boolean isAdmin = current.getRole() == Role.ADMIN;
        boolean isOwner = ticket.getCustomer() != null && ticket.getCustomer().getId().equals(current.getId());
        boolean isAssignedStaff = ticket.getAssignedStaff() != null && ticket.getAssignedStaff().getId().equals(current.getId());

        if (!(isAdmin || isOwner || isAssignedStaff)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to view this ticket's messages");
        }

        // 3) Return messages in chronological order
        return messageRepository.findByTicketIdOrderByTimestampAsc(ticketId);
    }

    @PostMapping("/tickets/{ticketId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message postMessage(@PathVariable Long ticketId,
                               @RequestParam("userId") Long userId,
                               @RequestParam("content") String content) {
        // 1) Load user and ticket
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        // 2) Permission: admin OR ticket owner OR assigned staff
        boolean isAdmin = current.getRole() == Role.ADMIN;
        boolean isOwner = ticket.getCustomer() != null && ticket.getCustomer().getId().equals(current.getId());
        boolean isAssignedStaff = ticket.getAssignedStaff() != null && ticket.getAssignedStaff().getId().equals(current.getId());
        if (!(isAdmin || isOwner || isAssignedStaff)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to reply on this ticket");
        }

        // 3) Block replies when CLOSED
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ticket is closed; replies are not allowed");
        }

        // 4) Validate content
        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content cannot be empty");
        }

        // 5) Create and save the message
        Message msg = new Message();
        msg.setTicket(ticket);
        msg.setSender(current);
        msg.setContent(content.trim());

        return messageRepository.save(msg);
    }
}
