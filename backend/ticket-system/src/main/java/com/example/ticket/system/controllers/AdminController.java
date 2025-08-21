package com.example.ticket.system.controllers;

import com.example.ticket.system.entities.Category;
import com.example.ticket.system.entities.Priority;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.User;
import com.example.ticket.system.service.TicketService;
import com.example.ticket.system.service.UserService;
import com.example.ticket.system.repositories.CategoryRepository;
import com.example.ticket.system.repositories.PriorityRepository;
import com.example.ticket.system.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired(required = false)
    private TicketService ticketService;

    // Promote user to staff
    @PutMapping("/users/{id}/promote/staff")
    public User promoteToStaff(@PathVariable Long id) {
        return userService.promoteToStaff(id);
    }

    // Promote user to admin
    @PutMapping("/users/{id}/promote/admin")
    public User promoteToAdmin(@PathVariable Long id) {
        return userService.promoteToAdmin(id);
    }

    // Manage Categories
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Manage Priorities
    @PostMapping("/priorities")
    public Priority createPriority(@RequestBody Priority priority) {
        return priorityRepository.save(priority);
    }

    @GetMapping("/priorities")
    public List<Priority> getAllPriorities() {
        return priorityRepository.findAll();
    }

    // View, filter, and search all tickets
    @GetMapping("/tickets")
    public List<Ticket> listTickets(@RequestParam(required = false) TicketStatus status,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(required = false) Long priorityId,
                                    @RequestParam(required = false) Long customerId,
                                    @RequestParam(required = false) Long staffId,
                                    @RequestParam(required = false, name = "query") String query) {
        if (ticketService == null) {
            throw new IllegalStateException("TicketService is not available");
        }
        return ticketService.listTickets(Optional.ofNullable(status),
                Optional.ofNullable(categoryId),
                Optional.ofNullable(priorityId),
                Optional.ofNullable(customerId),
                Optional.ofNullable(staffId),
                Optional.ofNullable(query));
    }
}