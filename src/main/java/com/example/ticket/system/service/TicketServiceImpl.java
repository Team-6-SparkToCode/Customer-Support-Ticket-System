package com.example.ticket.system.service;

import com.example.ticket.system.NotificationType;
import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.*;
import com.example.ticket.system.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        // Notify customer confirmation
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

        notificationService.notify(ticket.getCustomer(), ticket, NotificationType.REPLY,
                "Support replied to ticket #" + ticket.getId());
        return saved;
    }

    @Override
    @Transactional
    public Ticket updateStatus(Long ticketId, TicketStatus newStatus) {
        Ticket t = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));

        if (t.getStatus() == TicketStatus.CLOSED && newStatus == TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot move from CLOSED to IN_PROGRESS directly.");
        }

        t.setStatus(newStatus);
        Ticket saved = ticketRepository.save(t);

        notificationService.notify(t.getCustomer(), saved, NotificationType.STATUS_CHANGE,
                "Your ticket status changed to " + newStatus.name());

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

    @Override
    @Transactional
    public Ticket createTicket(Ticket ticket, Long customerId) {
        if (ticket == null || customerId == null) {
            throw new IllegalArgumentException("Ticket and customerId are required");
        }

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        if (!(user instanceof Customer)) {
            throw new IllegalArgumentException("User is not a customer: " + customerId);
        }

        if (ticket.getSubject() == null || ticket.getSubject().isBlank()
                || ticket.getDescription() == null || ticket.getDescription().isBlank()) {
            throw new IllegalArgumentException("Ticket subject and description are required");
        }

        ticket.setCustomer((Customer) user);
        ticket.setStatus(TicketStatus.OPEN);
        Ticket saved = ticketRepository.save(ticket);

        notificationService.notify(user, saved, NotificationType.NEW_TICKET,
                "Your ticket has been created: " + ticket.getSubject());

        return saved;
    }

    @Override
    public List<Ticket> getTicketsByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public Ticket getTicketForCustomer(Long ticketId, Long customerId) {
        return null;
    }

    @Override
    public Ticket updateStatusByCustomer(Long ticketId, Long customerId, TicketStatus ticketStatus) {
        return null;
    }

    @Override
    public Message addCustomerMessage(Long ticketId, Long customerId, String content) {
        return null;
    }


    // Replace your existing saveCSAT methods with these updated versions

    @Override
    @Transactional
    public void saveCSAT(Long ticketId, Integer speedScore, Integer qualityScore, Integer overallScore, String comment) {
        System.out.println("saveCSAT called with ticketId: " + ticketId +
                ", speed: " + speedScore +
                ", quality: " + qualityScore +
                ", overall: " + overallScore +
                ", comment: " + comment);

        // First, verify the ticket exists
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new IllegalArgumentException("Ticket not found: " + ticketId);
        }

        try {
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Attempting to update CSAT with timestamp: " + now);

            int rowsUpdated = ticketRepository.updateCSAT(ticketId, speedScore, qualityScore, overallScore, comment, now);

            System.out.println("Rows updated: " + rowsUpdated);

            if (rowsUpdated == 0) {
                throw new RuntimeException("No rows were updated for ticket: " + ticketId);
            }

            // Verify the update worked
            Object[] updatedCSAT = ticketRepository.getTicketCSATInfo(ticketId);
            System.out.println("Updated CSAT info: " + java.util.Arrays.toString(updatedCSAT));

            System.out.println("✅ CSAT updated successfully. Rows affected: " + rowsUpdated);

        } catch (Exception e) {
            System.out.println("❌ Error updating CSAT: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update CSAT for ticket " + ticketId + ": " + e.getMessage());
        }
    }

    // Alternative method using JPA entity update (more reliable)
    @Transactional
    public void saveCSATWithEntity(Long ticketId, Integer speedScore, Integer qualityScore, Integer overallScore, String comment) {
        System.out.println("saveCSATWithEntity called with ticketId: " + ticketId +
                ", speed: " + speedScore +
                ", quality: " + qualityScore +
                ", overall: " + overallScore +
                ", comment: " + comment);

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));

            // Update using entity (more reliable than native query)
            ticket.setCsatSpeedScore(speedScore);
            ticket.setCsatQualityScore(qualityScore);
            ticket.setCsatOverallScore(overallScore);
            ticket.setCsatComment(comment);
            ticket.setCsatSubmittedAt(LocalDateTime.now());

            Ticket saved = ticketRepository.save(ticket);

            System.out.println("✅ CSAT updated via entity. New values - Speed: " +
                    saved.getCsatSpeedScore() +
                    ", Quality: " + saved.getCsatQualityScore() +
                    ", Overall: " + saved.getCsatOverallScore() +
                    ", Comment: " + saved.getCsatComment());

        } catch (Exception e) {
            System.out.println("❌ Error updating CSAT via entity: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update CSAT for ticket " + ticketId + ": " + e.getMessage());
        }
    }


    // Optional: Add method for full CSAT scoring
    @Transactional
    public void saveFullCSAT(Long ticketId, Integer speedScore, Integer qualityScore, Integer overallScore, String comment) {
        try {
            int rowsUpdated = ticketRepository.updateCSAT(ticketId, speedScore, qualityScore, overallScore, comment, LocalDateTime.now());

            if (rowsUpdated == 0) {
                throw new RuntimeException("Ticket not found or not updated: " + ticketId);
            }

            System.out.println("✅ Full CSAT updated successfully. Rows affected: " + rowsUpdated);

        } catch (Exception e) {
            System.out.println("❌ Error updating full CSAT: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update full CSAT for ticket " + ticketId + ": " + e.getMessage());
        }
    }









}
