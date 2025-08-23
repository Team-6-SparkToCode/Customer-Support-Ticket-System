package com.example.ticket.system.controllers;

import com.example.ticket.system.entities.*;
import com.example.ticket.system.repositories.TicketRepository;
import com.example.ticket.system.service.FAQService;
import com.example.ticket.system.service.TicketService;
import com.example.ticket.system.service.UserService;
import com.example.ticket.system.service.FAQServiceImpl;
import com.example.ticket.system.repositories.CategoryRepository;
import com.example.ticket.system.repositories.PriorityRepository;
import com.example.ticket.system.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private FAQService faqService;

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

    // AdminController.java

    @Autowired private TicketRepository ticketRepository;

    @GetMapping("/csat/summary")
    public Map<String, Object> csatSummary(@RequestParam(required = false) Long staffId) {
        Double avg = (staffId == null)
                ? ticketRepository.avgCsat()
                : ticketRepository.avgCsatByStaff(staffId);
        Long count = ticketRepository.csatCount();

        Map<String, Object> resp = new HashMap<>();
        resp.put("average", avg);
        resp.put("count", count);
        resp.put("staffId", staffId);
        return resp;
    }
    // ===== FAQ MANAGEMENT =====

    // Get all FAQs for admin (includes inactive)
    @GetMapping("/faqs")
    public ResponseEntity<List<FAQ>> getAllFAQs() {
        try {
            List<FAQ> faqs = faqService.getAllFAQsForAdmin();
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            System.err.println("Error fetching FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get FAQ by ID
    @GetMapping("/faqs/{id}")
    public ResponseEntity<FAQ> getFAQById(@PathVariable Long id) {
        try {
            Optional<FAQ> faq = faqService.getFAQById(id);
            return faq.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error fetching FAQ: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Create new FAQ
    @PostMapping("/faqs")
    public ResponseEntity<?> createFAQ(@RequestBody FAQ faq,
                                       @RequestParam(required = false, defaultValue = "1") Long adminId) {
        try {
            System.out.println("Creating FAQ: " + faq.getQuestion());

            if (faq.getQuestion() == null || faq.getQuestion().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Question is required");
            }
            if (faq.getAnswer() == null || faq.getAnswer().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Answer is required");
            }

            FAQ createdFAQ = faqService.createFAQ(faq, adminId);
            return ResponseEntity.ok(createdFAQ);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating FAQ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating FAQ: " + e.getMessage());
        }
    }

    // Update FAQ
    @PutMapping("/faqs/{id}")
    public ResponseEntity<?> updateFAQ(@PathVariable Long id,
                                       @RequestBody FAQ faq,
                                       @RequestParam(required = false, defaultValue = "1") Long adminId) {
        try {
            System.out.println("Updating FAQ ID: " + id);

            FAQ updatedFAQ = faqService.updateFAQ(id, faq, adminId);
            return ResponseEntity.ok(updatedFAQ);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating FAQ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error updating FAQ: " + e.getMessage());
        }
    }

    // Delete FAQ
    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<?> deleteFAQ(@PathVariable Long id) {
        try {
            System.out.println("Deleting FAQ ID: " + id);

            faqService.deleteFAQ(id);
            return ResponseEntity.ok().body("FAQ deleted successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error deleting FAQ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting FAQ: " + e.getMessage());
        }
    }

    // Toggle FAQ active status
    @PutMapping("/faqs/{id}/toggle")
    public ResponseEntity<?> toggleFAQStatus(@PathVariable Long id) {
        try {
            System.out.println("Toggling FAQ status for ID: " + id);

            faqService.toggleFAQStatus(id);
            FAQ updated = faqService.getFAQById(id)
                    .orElseThrow(() -> new IllegalArgumentException("FAQ not found"));

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error toggling FAQ status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error toggling FAQ status: " + e.getMessage());
        }
    }

    // Get FAQ categories for admin
    @GetMapping("/faqs/categories")
    public ResponseEntity<List<String>> getFAQCategories() {
        try {
            List<String> categories = faqService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("Error fetching FAQ categories: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get FAQ statistics
    @GetMapping("/faqs/stats")
    public ResponseEntity<Map<String, Object>> getFAQStats() {
        try {
            List<FAQ> allFAQs = faqService.getAllFAQsForAdmin();
            List<FAQ> activeFAQs = faqService.getAllActiveFAQs();
            List<String> categories = faqService.getAllCategories();

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFAQs", allFAQs.size());
            stats.put("activeFAQs", activeFAQs.size());
            stats.put("inactiveFAQs", allFAQs.size() - activeFAQs.size());
            stats.put("totalCategories", categories.size());
            stats.put("categories", categories);

            // Calculate category distribution
            Map<String, Long> categoryStats = new HashMap<>();
            for (FAQ faq : allFAQs) {
                String category = faq.getCategory() != null ? faq.getCategory() : "Uncategorized";
                categoryStats.put(category, categoryStats.getOrDefault(category, 0L) + 1);
            }
            stats.put("categoryDistribution", categoryStats);

            // Calculate total view count
            int totalViews = allFAQs.stream()
                    .mapToInt(faq -> faq.getViewCount() != null ? faq.getViewCount() : 0)
                    .sum();
            stats.put("totalViews", totalViews);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("Error fetching FAQ statistics: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Search FAQs for admin (includes inactive FAQs)
    @GetMapping("/faqs/search")
    public ResponseEntity<List<FAQ>> searchFAQsAdmin(@RequestParam String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.ok(faqService.getAllFAQsForAdmin());
            }

            // Get all FAQs first, then filter by search term
            List<FAQ> allFAQs = faqService.getAllFAQsForAdmin();
            List<FAQ> filteredFAQs = allFAQs.stream()
                    .filter(faq ->
                            (faq.getQuestion() != null && faq.getQuestion().toLowerCase().contains(query.toLowerCase())) ||
                                    (faq.getAnswer() != null && faq.getAnswer().toLowerCase().contains(query.toLowerCase())) ||
                                    (faq.getCategory() != null && faq.getCategory().toLowerCase().contains(query.toLowerCase())) ||
                                    (faq.getTags() != null && faq.getTags().toLowerCase().contains(query.toLowerCase()))
                    )
                    .toList();

            return ResponseEntity.ok(filteredFAQs);
        } catch (Exception e) {
            System.err.println("Error searching FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get popular FAQs for admin analysis
    @GetMapping("/faqs/popular")
    public ResponseEntity<List<FAQ>> getPopularFAQs(@RequestParam(required = false, defaultValue = "10") int limit) {
        try {
            List<FAQ> popularFAQs = faqService.getPopularFAQs(limit);
            return ResponseEntity.ok(popularFAQs);
        } catch (Exception e) {
            System.err.println("Error fetching popular FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get FAQs by category for admin
    @GetMapping("/faqs/category/{category}")
    public ResponseEntity<List<FAQ>> getFAQsByCategory(@PathVariable String category) {
        try {
            // For admin, we want to see all FAQs in category (including inactive)
            List<FAQ> allFAQs = faqService.getAllFAQsForAdmin();
            List<FAQ> categoryFAQs = allFAQs.stream()
                    .filter(faq -> category.equals(faq.getCategory()))
                    .toList();

            return ResponseEntity.ok(categoryFAQs);
        } catch (Exception e) {
            System.err.println("Error fetching FAQs by category: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}