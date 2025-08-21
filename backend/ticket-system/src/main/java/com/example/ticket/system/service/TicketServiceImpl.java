package com.example.ticket.system.service;

import com.example.ticket.system.NotificationType;
import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.*;
import com.example.ticket.system.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PriorityRepository priorityRepository;
    private final NotificationService notificationService;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             MessageRepository messageRepository,
                             UserRepository userRepository,
                             CategoryRepository categoryRepository,
                             PriorityRepository priorityRepository,
                             NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.priorityRepository = priorityRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Ticket submitTicket(Long customerId, Long categoryId, Long priorityId, String subject, String description) {
        if (customerId == null || categoryId == null || priorityId == null) {
            throw new IllegalArgumentException("customerId, categoryId, and priorityId are required");
        }
        if (subject == null || subject.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException("subject and description are required");
        }

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("User is not a customer: " + customerId);
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
        Priority priority = priorityRepository.findById(priorityId)
                .orElseThrow(() -> new IllegalArgumentException("Priority not found: " + priorityId));

        Ticket ticket = new Ticket();
        ticket.setCustomer((Customer) user);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setSubject(subject);
        ticket.setDescription(description);
        ticket.setStatus(TicketStatus.OPEN);

        Ticket saved = ticketRepository.save(ticket);

        // Notify customer confirmation (self notification) or could email; here just persist notification
        notificationService.notify(user, saved, NotificationType.NEW_TICKET,
                "Your ticket has been created: " + subject);
        return saved;
    }

    @Override
    public List<Ticket> getCustomerTickets(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Message> getTicketThread(Long ticketId) {
        return messageRepository.findByTicketIdOrderByTimestampAsc(ticketId);
    }

    @Override
    @Transactional
    public Message customerReply(Long ticketId, Long customerId, String message) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("User is not a customer: " + customerId);
        }
        Message msg = new Message();
        msg.setTicket(ticket);
        msg.setSender(user);
        msg.setContent(message);
        Message saved = messageRepository.save(msg);

        // Notify assigned staff if any
        if (ticket.getAssignedStaff() != null) {
            notificationService.notify(ticket.getAssignedStaff(), ticket, NotificationType.REPLY,
                    "Customer replied on ticket #" + ticket.getId());
        }
        return saved;
    }

    @Override
    public List<Ticket> listTickets(Optional<TicketStatus> status,
                                    Optional<Long> categoryId,
                                    Optional<Long> priorityId,
                                    Optional<Long> customerId,
                                    Optional<Long> staffId,
                                    Optional<String> query) {
        // Simple filtering using repository methods; combine manually
        List<Ticket> base;
        if (query.isPresent() && !query.get().isBlank()) {
            base = ticketRepository.findBySubjectContainingIgnoreCase(query.get());
        } else if (status.isPresent()) {
            base = ticketRepository.findByStatus(status.get());
        } else if (customerId.isPresent()) {
            base = ticketRepository.findByCustomerId(customerId.get());
        } else if (staffId.isPresent()) {
            base = ticketRepository.findByAssignedStaffId(staffId.get());
        } else if (categoryId.isPresent()) {
            base = ticketRepository.findByCategoryId(categoryId.get());
        } else if (priorityId.isPresent()) {
            base = ticketRepository.findByPriorityId(priorityId.get());
        } else {
            base = ticketRepository.findAll();
        }

        // Apply remaining filters in-memory for minimal changes
        List<Ticket> filtered = new ArrayList<>(base);
        status.ifPresent(s -> filtered.removeIf(t -> t.getStatus() != s));
        categoryId.ifPresent(id -> filtered.removeIf(t -> t.getCategory() == null || !id.equals(t.getCategory().getId())));
        priorityId.ifPresent(id -> filtered.removeIf(t -> t.getPriority() == null || !id.equals(t.getPriority().getId())));
        customerId.ifPresent(id -> filtered.removeIf(t -> t.getCustomer() == null || !id.equals(t.getCustomer().getId())));
        staffId.ifPresent(id -> filtered.removeIf(t -> t.getAssignedStaff() == null || !id.equals(t.getAssignedStaff().getId())));
        if (query.isPresent() && !query.get().isBlank()) {
            String q = query.get().toLowerCase();
            filtered.removeIf(t -> t.getSubject() == null || !t.getSubject().toLowerCase().contains(q));
        }
        return filtered;
    }

    @Override
    @Transactional
    public Ticket assignTicket(Long ticketId, Long staffId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        User user = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffId));
        if (!(user instanceof Staff)) {
            throw new IllegalArgumentException("User is not staff: " + staffId);
        }
        ticket.setAssignedStaff((Staff) user);
        Ticket saved = ticketRepository.save(ticket);

        // Notify staff assignment
        notificationService.notify(user, saved, NotificationType.NEW_TICKET,
                "A ticket has been assigned to you: #" + saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Message staffReply(Long ticketId, Long staffId, String message) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        User user = userRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + staffId));
        if (!(user instanceof Staff)) {
            throw new IllegalArgumentException("User is not staff: " + staffId);
        }
        // On first staff reply, set IN_PROGRESS
        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticketRepository.save(ticket);
            notificationService.notify(ticket.getCustomer(), ticket, NotificationType.STATUS_CHANGE,
                    "Your ticket status changed to IN_PROGRESS");
        }

        Message msg = new Message();
        msg.setTicket(ticket);
        msg.setSender(user);
        msg.setContent(message);
        Message saved = messageRepository.save(msg);

        // Notify customer of reply
        notificationService.notify(ticket.getCustomer(), ticket, NotificationType.REPLY,
                "Support replied to ticket #" + ticket.getId());
        return saved;
    }

    @Override
    @Transactional
    public Ticket updateStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        ticket.setStatus(status);
        Ticket saved = ticketRepository.save(ticket);
        notificationService.notify(ticket.getCustomer(), saved, NotificationType.STATUS_CHANGE,
                "Your ticket status changed to " + status.name());
        return saved;
    }

    @Override
    public Ticket resolveTicket(Long ticketId) {
        return updateStatus(ticketId, TicketStatus.RESOLVED);
    }

    @Override
    public Ticket closeTicket(Long ticketId) {
        return updateStatus(ticketId, TicketStatus.CLOSED);
    }

    @Override
    public Ticket reopenTicket(Long ticketId) {
        return updateStatus(ticketId, TicketStatus.OPEN);
    }
}
