package com.example.ticket.system.repositories;

import com.example.ticket.system.TicketStatus;
import com.example.ticket.system.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCustomerId(Long customerId);
    List<Ticket> findByAssignedStaffId(Long staffId);
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByCategoryId(Long categoryId);
    List<Ticket> findByPriorityId(Long priorityId);
    List<Ticket> findBySubjectContainingIgnoreCase(String query);

    // Average CSAT overall score
    @Query(value = "SELECT AVG(csat_overall_score) FROM tickets WHERE csat_overall_score IS NOT NULL", nativeQuery = true)
    Double avgCsat();

    // Average CSAT by staff who handled
    @Query(value = "SELECT AVG(csat_overall_score) FROM tickets WHERE csat_overall_score IS NOT NULL AND assigned_staff_id = :staffId", nativeQuery = true)
    Double avgCsatByStaff(@Param("staffId") Long staffId);

    // Count CSAT submissions
    @Query(value = "SELECT COUNT(*) FROM tickets WHERE csat_overall_score IS NOT NULL", nativeQuery = true)
    Long csatCount();

    // Average speed score
    @Query(value = "SELECT AVG(csat_speed_score) FROM tickets WHERE csat_speed_score IS NOT NULL", nativeQuery = true)
    Double avgSpeedScore();

    // Average quality score
    @Query(value = "SELECT AVG(csat_quality_score) FROM tickets WHERE csat_quality_score IS NOT NULL", nativeQuery = true)
    Double avgQualityScore();

    // Get all CSAT scores for a ticket
    @Query(value = "SELECT csat_speed_score, csat_quality_score, csat_overall_score FROM tickets WHERE id = :ticketId", nativeQuery = true)
    Object[] getCsatScores(@Param("ticketId") Long ticketId);

    // Update CSAT with all three scores
    @Modifying
    @Transactional
    @Query(value = "UPDATE tickets SET csat_speed_score = :speedScore, csat_quality_score = :qualityScore, csat_overall_score = :overallScore, csat_comment = :comment, csat_submitted_at = :submittedAt WHERE id = :ticketId",
            nativeQuery = true)
    int updateCSAT(@Param("ticketId") Long ticketId,
                   @Param("speedScore") Integer speedScore,
                   @Param("qualityScore") Integer qualityScore,
                   @Param("overallScore") Integer overallScore,
                   @Param("comment") String comment,
                   @Param("submittedAt") LocalDateTime submittedAt);

    // Simpler update for just overall score (backward compatibility)
    @Modifying
    @Transactional
    @Query(value = "UPDATE tickets SET csat_overall_score = :rating, csat_comment = :comment, csat_submitted_at = :submittedAt WHERE id = :ticketId",
            nativeQuery = true)
    int updateSimpleCSAT(@Param("ticketId") Long ticketId,
                         @Param("rating") Integer rating,
                         @Param("comment") String comment,
                         @Param("submittedAt") LocalDateTime submittedAt);

    // Add this method to verify the ticket exists and check current CSAT values
    @Query(value = "SELECT id, csat_overall_score, csat_comment, csat_submitted_at FROM tickets WHERE id = :ticketId", nativeQuery = true)
    Object[] getTicketCSATInfo(@Param("ticketId") Long ticketId);
}