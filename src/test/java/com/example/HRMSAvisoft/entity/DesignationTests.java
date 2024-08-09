package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesignationTests {

    Long designationId;
    String designation;
    @BeforeEach
    void setUp(){
        designationId = 1L;
        designation = "JAVA DEVELOPER";
    }

    @Test
    @DisplayName("testDesignationConstructor")
    void testDesignationConstructor(){

        Designation newDesignation = new Designation(designationId, designation);

        assertEquals(designationId, newDesignation.getDesignationId());
        assertEquals(designation, newDesignation.getDesignation());
    }

    @Test
    @DisplayName("testDesignationGettersAndSetters")
    void testDesignationGettersAndSetters(){
        Designation newDesignation = new Designation();
        newDesignation.setDesignationId(designationId);
        newDesignation.setDesignation(designation);

        assertEquals(designationId, newDesignation.getDesignationId());
        assertEquals(designation, newDesignation.getDesignation());
    }
}
