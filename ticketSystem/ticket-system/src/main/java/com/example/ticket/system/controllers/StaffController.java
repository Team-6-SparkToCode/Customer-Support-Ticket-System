package com.example.ticket.system.controllers;

import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.service.TicketService;
import com.example.ticket.system.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private TicketService ticketService;

    // View, filter, and search tickets
    @GetMapping("/tickets")
    public List<Ticket> listTickets(@RequestParam(required = false) TicketStatus status,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) Long priorityId,
                                    @RequestParam(required = false) Long customerId,
                                    @RequestParam(required = false) Long staffId,
                                    @RequestParam(required = false, name = "query") String query) {
        return ticketService.listTickets(Optional.ofNullable(status),
                Optional.ofNullable(categoryId),
                Optional.ofNullable(priorityId),
                Optional.ofNullable(customerId),
                Optional.ofNullable(staffId),
                Optional.ofNullable(query));
    }

    // Assign a ticket to a staff member
    @PutMapping("/tickets/{ticketId}/assign/{staffId}")
    public Ticket assignTicket(@PathVariable Long ticketId, @PathVariable Long staffId) {
        return ticketService.assignTicket(ticketId, staffId);
    }

    // Update ticket status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
    @PutMapping("/tickets/{ticketId}/status")
    public Ticket updateTicketStatus(@PathVariable Long ticketId, @RequestParam TicketStatus status) {
        return ticketService.updateStatus(ticketId, status);
    }

    // Respond to ticket
    @PostMapping("/tickets/{ticketId}/reply")
    public void replyToTicket(@PathVariable Long ticketId,
                              @RequestParam Long staffId,
                              @RequestParam String message) {
        ticketService.staffReply(ticketId, staffId, message);
    }
}
