package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Employee;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class DesignationRepositoryTests {
    @Autowired
    DesignationRepository designationRepository;


    String designationName = "JAVA Developer";
    Designation newDesignation;

    @BeforeEach
    void setup(){
        Designation designation = new Designation();
        designation.setDesignation(designationName);

        newDesignation = designationRepository.save(designation);
    }

    @Test
    @DisplayName("testSaveDesignation")
    void saveDesignation(){
        assertEquals(designationName, newDesignation.getDesignation());
    }

    @Test
    @DisplayName("testGetDesignationById")
    void getDesignationById(){

        Designation designationFoundById = designationRepository.findById(newDesignation.getDesignationId()).orElse(null);

        assertNotNull(designationFoundById);
        assertEquals(designationName, designationFoundById.getDesignation());

    }

    @Test
    @DisplayName("testUpdateDesignation")
    void updateDesignation()throws EntityNotFoundException {

        Designation designationFoundById = designationRepository.findById(newDesignation.getDesignationId()).orElseThrow(()-> new EntityNotFoundException("Department not found"));

        assertNotNull(designationFoundById);

        String newDesignationName = "MERN developer";

        designationFoundById.setDesignation(newDesignationName);

        Designation updatedDesignation = designationRepository.save(designationFoundById);

        assertEquals(newDesignationName, updatedDesignation.getDesignation());
    }

    @Test
    @DisplayName("testDeleteDesignation")
    void deleteDesignation(){
        designationRepository.delete(newDesignation);

        Designation deletedDesignation = designationRepository.findById(newDesignation.getDesignationId()).orElse((null));

        assertNull(deletedDesignation);

    }

}
