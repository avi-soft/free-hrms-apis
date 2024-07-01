package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.entity.Rating;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class PerformanceRespositoryTests {

    @Autowired
    PerformanceRepository performanceRepository;

    Long performanceId;
    Employee employee;
    String reviewDate;
    Employee reviewer;
    Rating rating;
    String comment;

    @BeforeEach
    void setUp() throws Exception {
        performanceId = 1L;
        employee = new Employee();
        reviewDate = "12/12/2023 00:00:00";
        reviewer = new Employee();
        rating = Rating.GOOD;
        comment = "Good performance";
    }

    @Test
    @DisplayName("test_savePerformance")
    void test_savePerformance() throws Exception {
        Performance performance = new Performance();
        performance.setEmployee(employee);
        performance.setReviewer(reviewer);
        performance.setComment(comment);
        performance.setReviewDate(reviewDate);
        performance.setRating(rating);

        Performance newPerformance = performanceRepository.save(performance);

        Performance performanceFoundById = performanceRepository.findById(newPerformance.getPerformanceId()).orElse(null);

        assertEquals(performanceFoundById.getPerformanceId(), newPerformance.getPerformanceId());
        assertEquals(employee, newPerformance.getEmployee());
        assertEquals(reviewDate, newPerformance.getReviewDate());
        assertEquals(reviewer, newPerformance.getReviewer());
        assertEquals(rating, newPerformance.getRating());
        assertEquals(comment, newPerformance.getComment());
    }

    @Test
    @DisplayName("test_getPerformanceById")
    void test_getPerformanceById() throws Exception {
        Performance performance = new Performance();
        performance.setEmployee(employee);
        performance.setReviewer(reviewer);
        performance.setComment(comment);
        performance.setReviewDate(reviewDate);
        performance.setRating(rating);


        Performance newPerformance = performanceRepository.save(performance);

        Performance performanceFoundById = performanceRepository.findById(newPerformance.getPerformanceId()).orElse(null);

        assertEquals(newPerformance.getPerformanceId(), performanceFoundById.getPerformanceId());
        assertEquals(newPerformance.getEmployee(), performanceFoundById.getEmployee());
        assertEquals(newPerformance.getReviewer(), performanceFoundById.getReviewer());
        assertEquals(newPerformance.getRating(), performanceFoundById.getRating());
        assertEquals(newPerformance.getComment(), performanceFoundById.getComment());
        assertEquals(newPerformance.getReviewDate(), performanceFoundById.getReviewDate());

    }

    @Test
    @DisplayName("test_updatePerformance")
    void test_updatePerformance() throws Exception {
        Performance performance = new Performance();
        performance.setEmployee(employee);
        performance.setReviewer(reviewer);
        performance.setComment(comment);
        performance.setReviewDate(reviewDate);
        performance.setRating(rating);


        Performance newPerformance = performanceRepository.save(performance);

        Performance performanceFoundById = performanceRepository.findById(newPerformance.getPerformanceId()).orElse(null);

        String updatedComment = "updated Performance";
        Rating updatedRating = Rating.EXCELLENT;

        performanceFoundById.setComment(updatedComment);
        performanceFoundById.setRating(updatedRating);

        Performance updatedPerformance = performanceRepository.save(performanceFoundById);

        assertEquals(updatedComment, updatedPerformance.getComment());
        assertEquals(updatedRating, updatedPerformance.getRating());
    }
}
