package com.example.ticket.system.service;

import com.example.ticket.system.entities.FAQ;
import java.util.List;
import java.util.Optional;

public interface FAQService {
    // Public methods
    List<FAQ> getAllActiveFAQs();
    List<FAQ> getFAQsByCategory(String category);
    List<String> getAllCategories();
    List<FAQ> searchFAQs(String searchTerm);
    Optional<FAQ> getFAQById(Long id);
    void incrementViewCount(Long id);
    List<FAQ> getPopularFAQs(int limit);

    // Admin methods
    List<FAQ> getAllFAQsForAdmin();
    FAQ createFAQ(FAQ faq, Long adminId);
    FAQ updateFAQ(Long id, FAQ faq, Long adminId);
    void deleteFAQ(Long id);
    void toggleFAQStatus(Long id);
}