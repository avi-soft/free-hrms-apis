package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.repository.DesignationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)

public class DesignationServiceTests {

    @InjectMocks
    DesignationService designationService;

    @Mock
    DesignationRepository designationRepository;

        @Test
        @DisplayName("test_handles_special_characters_input")
        public void test_handles_special_characters_input() {

            Designation designation = new Designation();
            designation.setDesignation("MANAGER");

            Mockito.when(designationRepository.save(Mockito.any(Designation.class))).thenReturn(designation);

            Designation result = designationService.addDesignation(designation);

            assertNotNull(result);
            assertEquals("MANAGER", result.getDesignation());
        }

    @Test
    @DisplayName("retrieves_all_designations")
    public void retrieves_all_designations() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);

        List<Designation> designations = Arrays.asList(
                new Designation(1L, "Manager"),
                new Designation(2L, "Developer")
        );
        Mockito.when(mockRepository.findAll()).thenReturn(designations);

        List<Designation> result = service.getAllDesignations();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Manager", result.get(0).getDesignation());
        Assertions.assertEquals("Developer", result.get(1).getDesignation());
    }

    // Returns an empty list if no designations are found
    @Test
    public void returns_empty_list_if_no_designations() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);

        Mockito.when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        List<Designation> result = service.getAllDesignations();

        Assertions.assertTrue(result.isEmpty());
    }

}


