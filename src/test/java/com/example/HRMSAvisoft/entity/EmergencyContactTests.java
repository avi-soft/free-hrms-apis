package com.example.HRMSAvisoft.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmergencyContactTests {
    
    @Test
    @DisplayName("test_EmergencyContactConstructor")
    void test_EmergencyContactConstructor(){
        Long emergencyContactId = 1L;
        String contact = "9820640927";
        String relationship = "father";

        EmergencyContact emergencyContact = new EmergencyContact(emergencyContactId, contact, relationship);

        assertEquals(emergencyContactId, emergencyContact.getEmergencyContactId());
        assertEquals(contact, emergencyContact.getContact());
        assertEquals(relationship, emergencyContact.getRelationship());
    }

    @Test
    @DisplayName("test_EmergencyContactGetterSetters")
    void test_EmergencyContactGetterSetters(){

        Long emergencyContactId = 1L;
        String contact = "9820640927";
        String relationship = "father";

        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setEmergencyContactId(emergencyContactId);
        emergencyContact.setContact(contact);
        emergencyContact.setRelationship(relationship);

        assertEquals(emergencyContactId, emergencyContact.getEmergencyContactId());
        assertEquals(contact, emergencyContact.getContact());
        assertEquals(relationship, emergencyContact.getRelationship());

    }
}
