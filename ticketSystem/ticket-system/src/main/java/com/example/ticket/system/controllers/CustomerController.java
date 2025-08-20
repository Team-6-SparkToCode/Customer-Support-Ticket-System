package com.example.ticket.system.controller;

import com.example.ticket.system.entities.Customer;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private TicketService ticketService;

    // Current logged-in customer profile
    @GetMapping("/me")
    public Customer getCurrentCustomer(@AuthenticationPrincipal Customer customer) {
        return customer;
    }

    // Get tickets for logged-in customer
    @GetMapping("/me/tickets")
    public List<Ticket> getMyTickets(@AuthenticationPrincipal Customer customer) {
        return ticketService.getCustomerTickets(customer.getId());
    }

    // Submit a new ticket
    @PostMapping("/me/tickets")
    public Ticket createTicket(
            @AuthenticationPrincipal Customer customer,
            @RequestParam Long categoryId,
            @RequestParam Long priorityId,
            @RequestParam String subject,
            @RequestParam String description) {
        return ticketService.submitTicket(customer.getId(), categoryId, priorityId, subject, description);
    }
}
