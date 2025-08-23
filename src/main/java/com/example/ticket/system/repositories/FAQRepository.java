package com.example.ticket.system.repositories;

import com.example.ticket.system.entities.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    // Find active FAQs
    List<FAQ> findByIsActiveTrueOrderByViewCountDescCreatedAtDesc();

    // Find by category
    List<FAQ> findByCategoryAndIsActiveTrueOrderByViewCountDesc(String category);

    // Get all categories
    @Query("SELECT DISTINCT f.category FROM FAQ f WHERE f.isActive = true AND f.category IS NOT NULL ORDER BY f.category")
    List<String> findDistinctCategories();

    // Search FAQs - full text search
    @Query(value = "SELECT * FROM faqs WHERE is_active = true AND " +
            "MATCH(question, answer, tags) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE) " +
            "ORDER BY view_count DESC, created_at DESC", nativeQuery = true)
    List<FAQ> searchFAQs(@Param("searchTerm") String searchTerm);

    // Fallback search using LIKE (in case full-text search doesn't work)
    @Query("SELECT f FROM FAQ f WHERE f.isActive = true AND " +
            "(LOWER(f.question) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(f.answer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(f.tags) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "ORDER BY f.viewCount DESC, f.createdAt DESC")
    List<FAQ> searchFAQsLike(@Param("searchTerm") String searchTerm);

    // Increment view count
    @Modifying
    @Transactional
    @Query("UPDATE FAQ f SET f.viewCount = f.viewCount + 1 WHERE f.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // Get popular FAQs
    @Query("SELECT f FROM FAQ f WHERE f.isActive = true ORDER BY f.viewCount DESC")
    List<FAQ> findPopularFAQs();

    // Admin queries - include inactive FAQs
    List<FAQ> findAllByOrderByCreatedAtDesc();

    List<FAQ> findByCategoryOrderByCreatedAtDesc(String category);
}