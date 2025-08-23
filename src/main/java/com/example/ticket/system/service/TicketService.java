package com.example.ticket.system.service;

import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Message;
import com.example.ticket.system.entities.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    // ---- Customer actions ----
    Ticket submitTicket(Long customerId, Long categoryId, Long priorityId, String subject, String description);

    List<Ticket> getCustomerTickets(Long customerId);

    List<Message> getTicketThread(Long ticketId);

    Message customerReply(Long ticketId, Long customerId, String message);

    // ---- Staff/Admin actions ----
    List<Ticket> listTickets(Optional<TicketStatus> status,
                             Optional<Long> categoryId,
                             Optional<Long> priorityId,
                             Optional<Long> customerId,
                             Optional<Long> staffId,
                             Optional<String> query);

    Ticket assignTicket(Long ticketId, Long staffId);

    Message staffReply(Long ticketId, Long staffId, String message);

    Ticket updateStatus(Long ticketId, TicketStatus newStatus);

    Ticket resolveTicket(Long ticketId);

    Ticket closeTicket(Long ticketId);

    Ticket reopenTicket(Long ticketId);

    // ---- Extra helpers ----
    Ticket createTicket(Ticket ticket, Long customerId);

    List<Ticket> getTicketsByCustomer(Long customerId);

    Ticket getTicketForCustomer(Long ticketId, Long customerId);

    Ticket updateStatusByCustomer(Long ticketId, Long customerId, TicketStatus ticketStatus);

    Message addCustomerMessage(Long ticketId, Long customerId, String content);
    void saveCSAT(Long ticketId, Integer speedScore, Integer qualityScore, Integer overallScore, String comment);
    void saveFullCSAT(Long ticketId, Integer speedScore, Integer qualityScore, Integer overallScore, String comment);
}
