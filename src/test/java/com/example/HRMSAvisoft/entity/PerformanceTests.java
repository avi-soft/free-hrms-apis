package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerformanceTests {

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
    @DisplayName("test_performanceConstructor")
    void test_PerformanceConstructor() throws Exception {


        Performance performance = new Performance(performanceId, employee, reviewDate, reviewer, rating, comment);

        assertEquals(performanceId, performance.getPerformanceId());
        assertEquals(reviewDate, performance.getReviewDate());
        assertEquals(reviewer, performance.getReviewer());
        assertEquals(rating, performance.getRating());
        assertEquals(comment, performance.getComment());
        assertEquals(employee, performance.getEmployee());
    }


    @Test
    @DisplayName("test_getterSettersOfPerformance")
    void testGetterSettersOfPerformance(){

        Performance performance = new Performance();

        performance.setPerformanceId(performanceId);
        performance.setEmployee(employee);
        performance.setReviewer(reviewer);
        performance.setComment(comment);
        performance.setReviewDate(reviewDate);
        performance.setRating(rating);

        assertEquals(performanceId, performance.getPerformanceId());
        assertEquals(reviewDate, performance.getReviewDate());
        assertEquals(reviewer, performance.getReviewer());
        assertEquals(rating, performance.getRating());
        assertEquals(comment, performance.getComment());
        assertEquals(employee, performance.getEmployee());
    }
}

