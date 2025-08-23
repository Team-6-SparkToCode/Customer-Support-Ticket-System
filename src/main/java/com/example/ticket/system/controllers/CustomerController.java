package com.example.ticket.system.controllers;

import com.example.ticket.system.CSATReqDTO;
import com.example.ticket.system.entities.Ticket;
import com.example.ticket.system.entities.Message;
import com.example.ticket.system.service.TicketService;
import com.example.ticket.system.TicketStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private TicketService ticketService;

    // ---------------------------
    // 1. Create a new ticket
    // ---------------------------
    @PostMapping("/{customerId}/tickets")
    public Ticket createTicket(@PathVariable Long customerId, @RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket, customerId);
    }

    // ---------------------------
    // 2. Get all tickets for a customer
    // ---------------------------
    @GetMapping("/{customerId}/tickets")
    public List<Ticket> getCustomerTickets(@PathVariable Long customerId) {
        return ticketService.getTicketsByCustomer(customerId);
    }

    // ---------------------------
    // 3. Get single ticket details
    // ---------------------------
    @GetMapping("/{customerId}/tickets/{ticketId}")
    public Ticket getTicket(@PathVariable Long customerId, @PathVariable Long ticketId) {
        return ticketService.getTicketForCustomer(ticketId, customerId);
    }

    // ---------------------------
    // 4. Add a message (reply) to ticket
    // ---------------------------
    @PostMapping("/{customerId}/tickets/{ticketId}/messages")
    public Message addMessage(@PathVariable Long customerId,
                              @PathVariable Long ticketId,
                              @RequestBody String content) {
        return ticketService.addCustomerMessage(ticketId, customerId, content);
    }

    // ---------------------------
    // 5. Customer closes their ticket
    // ---------------------------
    @PutMapping("/{customerId}/tickets/{ticketId}/close")
    public Ticket closeTicket(@PathVariable Long customerId, @PathVariable Long ticketId) {
        return ticketService.updateStatusByCustomer(ticketId, customerId, TicketStatus.CLOSED);
    }

    @PostMapping("/tickets/{ticketId}/csat")
    @Transactional
    public ResponseEntity<?> submitCSAT(@PathVariable Long ticketId, @RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== CONTROLLER DEBUG ===");
            System.out.println("Received CSAT for ticket " + ticketId);
            System.out.println("Full request body: " + request);

            // Print all keys and values for debugging
            for (Map.Entry<String, Object> entry : request.entrySet()) {
                System.out.println("Key: '" + entry.getKey() + "' -> Value: '" + entry.getValue() + "' (Type: " +
                        (entry.getValue() != null ? entry.getValue().getClass().getSimpleName() : "null") + ")");
            }

            // Extract values with better error handling
            Long customerId = null;
            if (request.get("customerId") != null) {
                customerId = Long.valueOf(request.get("customerId").toString());
            }

            Integer speedScore = null;
            if (request.get("speed") != null) {
                speedScore = Integer.valueOf(request.get("speed").toString());
            }

            Integer qualityScore = null;
            if (request.get("quality") != null) {
                qualityScore = Integer.valueOf(request.get("quality").toString());
            }

            Integer overallScore = null;
            if (request.get("overall") != null) {
                overallScore = Integer.valueOf(request.get("overall").toString());
            }

            String comment = null;
            if (request.get("comment") != null) {
                comment = request.get("comment").toString().trim();
                if (comment.isEmpty()) {
                    comment = null;
                }
            }

            System.out.println("Extracted values:");
            System.out.println("Speed: " + speedScore);
            System.out.println("Quality: " + qualityScore);
            System.out.println("Overall: " + overallScore);
            System.out.println("Comment: " + comment);

            // Validate required fields
            if (speedScore == null || qualityScore == null || overallScore == null) {
                String missingFields = "";
                if (speedScore == null) missingFields += "speed ";
                if (qualityScore == null) missingFields += "quality ";
                if (overallScore == null) missingFields += "overall ";

                System.out.println("❌ Missing required fields: " + missingFields);
                return ResponseEntity.badRequest().body("Missing required rating fields: " + missingFields);
            }

            // Validate ranges
            if (speedScore < 1 || speedScore > 5 ||
                    qualityScore < 1 || qualityScore > 5 ||
                    overallScore < 1 || overallScore > 5) {
                return ResponseEntity.badRequest().body("All ratings must be between 1 and 5");
            }

            // Save CSAT
            ticketService.saveFullCSAT(ticketId, speedScore, qualityScore, overallScore, comment);

            return ResponseEntity.ok().body("CSAT feedback submitted successfully");

        } catch (Exception e) {
            System.err.println("❌ Controller error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
