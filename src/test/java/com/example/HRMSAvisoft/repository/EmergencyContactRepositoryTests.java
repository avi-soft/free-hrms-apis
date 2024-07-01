package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class EmergencyContactRepositoryTests {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    String contact = "9820640927";
    String relationship = "father";

    @Test
    @DisplayName("test_saveEmergencyContact")
    void testSaveEmergencyContact() throws Exception {
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setRelationship(relationship);
        emergencyContact.setContact(contact);

        EmergencyContact savedEmergencyContact = emergencyContactRepository.save(emergencyContact);

        assertEquals(savedEmergencyContact.getContact(), emergencyContact.getContact());
        assertEquals(savedEmergencyContact.getRelationship(), emergencyContact.getRelationship());
    }

    @Test
    @DisplayName("test_getEmergencyContactsById")
    void testGetEmergencyContactsById() {

        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setRelationship(relationship);
        emergencyContact.setContact(contact);

        EmergencyContact savedEmergencyContact = emergencyContactRepository.save(emergencyContact);

        EmergencyContact emergencyContactFound = emergencyContactRepository.findById(savedEmergencyContact.getEmergencyContactId()).get();

        assertEquals(savedEmergencyContact.getEmergencyContactId(), emergencyContactFound.getEmergencyContactId());
        assertEquals("9820640927", emergencyContactFound.getContact());
        assertEquals("father", emergencyContactFound.getRelationship());
    }

    @Test
    @DisplayName("test_updateEmergencyContact")
    void testUpdateEmergencyContact(){
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setRelationship(relationship);
        emergencyContact.setContact(contact);

        EmergencyContact savedEmergencyContact = emergencyContactRepository.save(emergencyContact);

        EmergencyContact emergencyContactFound = emergencyContactRepository.findById(savedEmergencyContact.getEmergencyContactId()).get();

        String relationToUpdate = "brother";
        String contactToUpdate = "9850833536";

        emergencyContactFound.setRelationship(relationToUpdate);
        emergencyContactFound.setContact(contactToUpdate);
        EmergencyContact updatedEmergencyContact = emergencyContactRepository.save(emergencyContactFound);

        assertNotNull(updatedEmergencyContact);
        assertEquals(relationToUpdate, updatedEmergencyContact.getRelationship());
        assertEquals(contactToUpdate, updatedEmergencyContact.getContact());
    }

}
