package com.example.ticket.system.controllers;

import com.example.ticket.system.entities.FAQ;
import com.example.ticket.system.service.FAQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicFAQController {

    @Autowired
    private FAQService faqService;

    // Get all active FAQs
    @GetMapping("/faqs")
    public ResponseEntity<List<FAQ>> getAllFAQs() {
        try {
            List<FAQ> faqs = faqService.getAllActiveFAQs();
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            System.err.println("Error fetching public FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Search FAQs
    @GetMapping("/faqs/search")
    public ResponseEntity<List<FAQ>> searchFAQs(@RequestParam("q") String query) {
        try {
            System.out.println("Searching FAQs with query: " + query);
            List<FAQ> results = faqService.searchFAQs(query);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.err.println("Error searching FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get FAQs by category
    @GetMapping("/faqs/category/{category}")
    public ResponseEntity<List<FAQ>> getFAQsByCategory(@PathVariable String category) {
        try {
            List<FAQ> faqs = faqService.getFAQsByCategory(category);
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            System.err.println("Error fetching FAQs by category: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get all FAQ categories
    @GetMapping("/faqs/categories")
    public ResponseEntity<List<String>> getCategories() {
        try {
            List<String> categories = faqService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("Error fetching FAQ categories: " + e.getMessage());
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
            System.err.println("Error fetching FAQ by ID: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Increment view count
    @PostMapping("/faqs/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long id) {
        try {
            faqService.incrementViewCount(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error incrementing view count: " + e.getMessage());
            return ResponseEntity.ok().build(); // Don't fail the request for view count issues
        }
    }

    // Get popular FAQs
    @GetMapping("/faqs/popular")
    public ResponseEntity<List<FAQ>> getPopularFAQs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<FAQ> popularFAQs = faqService.getPopularFAQs(limit);
            return ResponseEntity.ok(popularFAQs);
        } catch (Exception e) {
            System.err.println("Error fetching popular FAQs: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get FAQ statistics for public display
    @GetMapping("/faqs/stats")
    public ResponseEntity<Map<String, Object>> getFAQStats() {
        try {
            List<FAQ> allFAQs = faqService.getAllActiveFAQs();
            List<String> categories = faqService.getAllCategories();

            int totalViews = allFAQs.stream()
                    .mapToInt(faq -> faq.getViewCount() != null ? faq.getViewCount() : 0)
                    .sum();

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFAQs", allFAQs.size());
            stats.put("totalCategories", categories.size());
            stats.put("totalViews", totalViews);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("Error fetching FAQ stats: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}