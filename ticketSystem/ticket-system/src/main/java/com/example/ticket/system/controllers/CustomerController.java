package com.example.ticket.system.controller;

import com.example.ticket.system.entities.Customer;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.service.CustomerService;
import com.example.ticket.system.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TicketService ticketService;

    // Endpoint: current logged-in customer profile
    @GetMapping("/me")
    public Customer getCurrentCustomer(@AuthenticationPrincipal Customer customer) {
        return customer;
    }

    // Endpoint: get tickets for logged-in customer
    @GetMapping("/me/tickets")
    public List<Ticket> getMyTickets(@AuthenticationPrincipal Customer customer) {
        return ticketService.getTicketsByUserId(customer.getId());
    }

    // Endpoint: create ticket (customer action)
    @PostMapping("/me/tickets")
    public Ticket createTicket(@AuthenticationPrincipal Customer customer, @RequestBody Ticket ticket) {
        return ticketService.createTicketForUser(customer.getId(), ticket);
    }
}
