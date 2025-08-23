package com.example.ticket.system.service;

import com.example.ticket.system.entities.FAQ;
import com.example.ticket.system.repositories.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FAQServiceImpl implements FAQService {

    @Autowired
    private FAQRepository faqRepository;

    @Override
    public List<FAQ> getAllActiveFAQs() {
        return faqRepository.findByIsActiveTrueOrderByViewCountDescCreatedAtDesc();
    }

    @Override
    public List<FAQ> getFAQsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return getAllActiveFAQs();
        }
        return faqRepository.findByCategoryAndIsActiveTrueOrderByViewCountDesc(category);
    }

    @Override
    public List<String> getAllCategories() {
        return faqRepository.findDistinctCategories();
    }

    @Override
    public List<FAQ> searchFAQs(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllActiveFAQs();
        }

        try {
            // Try full-text search first
            List<FAQ> results = faqRepository.searchFAQs(searchTerm.trim());
            if (results.isEmpty()) {
                // Fall back to LIKE search
                results = faqRepository.searchFAQsLike(searchTerm.trim());
            }
            return results;
        } catch (Exception e) {
            // If full-text search fails, use LIKE search
            return faqRepository.searchFAQsLike(searchTerm.trim());
        }
    }

    @Override
    public Optional<FAQ> getFAQById(Long id) {
        return faqRepository.findById(id);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        try {
            faqRepository.incrementViewCount(id);
        } catch (Exception e) {
            // If increment fails, fetch and update manually
            Optional<FAQ> faqOpt = faqRepository.findById(id);
            if (faqOpt.isPresent()) {
                FAQ faq = faqOpt.get();
                faq.setViewCount(faq.getViewCount() + 1);
                faqRepository.save(faq);
            }
        }
    }

    @Override
    public List<FAQ> getPopularFAQs(int limit) {
        List<FAQ> popular = faqRepository.findPopularFAQs();
        return popular.size() > limit ? popular.subList(0, limit) : popular;
    }

    // Admin methods
    @Override
    public List<FAQ> getAllFAQsForAdmin() {
        return faqRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional
    public FAQ createFAQ(FAQ faq, Long adminId) {
        if (faq.getQuestion() == null || faq.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Question is required");
        }
        if (faq.getAnswer() == null || faq.getAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Answer is required");
        }

        faq.setCreatedByAdminId(adminId);
        faq.setCreatedAt(LocalDateTime.now());
        faq.setUpdatedAt(LocalDateTime.now());

        if (faq.getIsActive() == null) {
            faq.setIsActive(true);
        }
        if (faq.getViewCount() == null) {
            faq.setViewCount(0);
        }

        return faqRepository.save(faq);
    }

    @Override
    @Transactional
    public FAQ updateFAQ(Long id, FAQ faq, Long adminId) {
        FAQ existing = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + id));

        if (faq.getQuestion() != null && !faq.getQuestion().trim().isEmpty()) {
            existing.setQuestion(faq.getQuestion().trim());
        }
        if (faq.getAnswer() != null && !faq.getAnswer().trim().isEmpty()) {
            existing.setAnswer(faq.getAnswer().trim());
        }
        if (faq.getCategory() != null) {
            existing.setCategory(faq.getCategory().trim());
        }
        if (faq.getTags() != null) {
            existing.setTags(faq.getTags().trim());
        }
        if (faq.getIsActive() != null) {
            existing.setIsActive(faq.getIsActive());
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return faqRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteFAQ(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new IllegalArgumentException("FAQ not found: " + id);
        }
        faqRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleFAQStatus(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found: " + id));

        faq.setIsActive(!faq.getIsActive());
        faq.setUpdatedAt(LocalDateTime.now());
        faqRepository.save(faq);
    }
}
