package com.example.ticket.system.repositories;

import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCustomerId(Long customerId);
    List<Ticket> findByAssignedStaffId(Long staffId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByCategoryId(Long categoryId);
    List<Ticket> findByPriorityId(Long priorityId);
    List<Ticket> findBySubjectContainingIgnoreCase(String query);
}
