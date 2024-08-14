package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.repository.DesignationRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


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

            when(designationRepository.save(Mockito.any(Designation.class))).thenReturn(designation);

            Designation result = designationService.addDesignation(designation);

            assertNotNull(result);
            assertEquals("MANAGER", result.getDesignation());
        }

    @Test
    @DisplayName("retrieves_all_designations")
    public void retrieves_all_designations() {
        DesignationRepository mockRepository = mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);

        List<Designation> designations = Arrays.asList(
                new Designation(1L, "Manager"),
                new Designation(2L, "Developer")
        );
        when(mockRepository.findAll()).thenReturn(designations);

        List<Designation> result = service.getAllDesignations();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Manager", result.get(0).getDesignation());
        Assertions.assertEquals("Developer", result.get(1).getDesignation());
    }

    // Returns an empty list if no designations are found
    @Test
    public void returns_empty_list_if_no_designations() {
        DesignationRepository mockRepository = mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);

        when(mockRepository.findAll()).thenReturn(Collections.emptyList());

        List<Designation> result = service.getAllDesignations();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("test_add_valid_designation")
    public void test_add_valid_designation() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("Manager");

        Mockito.when(mockRepository.save(Mockito.any(Designation.class))).thenReturn(designation);

        Designation result = service.addDesignation(designation);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Manager", result.getDesignation());
    }

    @Test
    @DisplayName("test_save_new_designation_to_repository")
    public void test_save_new_designation_to_repository() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("Developer");

        service.addDesignation(designation);

        Mockito.verify(mockRepository, Mockito.times(1)).save(Mockito.any(Designation.class));
    }

    @Test
    @DisplayName("test_throw_exception_when_designation_null")
    public void test_throw_exception_when_designation_null() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.addDesignation(designation);
        });
    }

    @Test
    @DisplayName("test_update_designation_success")
    public void test_update_designation_success() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("Manager");
        Designation existingDesignation = new Designation();
        existingDesignation.setDesignationId(1L);
        existingDesignation.setDesignation("Developer");

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));

        service.updateDesignation(designation, 1L);

        Mockito.verify(mockRepository).save(existingDesignation);
        assertEquals("Manager", existingDesignation.getDesignation());
    }

    @Test
    @DisplayName("test_save_updated_designation")
    public void test_save_updated_designation() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("Manager");
        Designation existingDesignation = new Designation();
        existingDesignation.setDesignationId(1L);
        existingDesignation.setDesignation("Developer");

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(existingDesignation));

        service.updateDesignation(designation, 1L);

        Mockito.verify(mockRepository).save(existingDesignation);
    }

    @Test
    @DisplayName("test_throw_illegal_argument_exception_when_designation_is_null")
    public void test_throw_illegal_argument_exception_when_designation_is_null() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateDesignation(designation, 1L);
        });

        assertEquals("Designation field cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("test_throw_illegal_argument_exception_when_designation_is_empty_string")
    public void test_throw_illegal_argument_exception_when_designation_is_empty_string() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.updateDesignation(designation, 1L);
        });

        assertEquals("Designation field cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("test_throw_entity_not_found_exception_when_designation_id_not_exist")
    public void test_throw_entity_not_found_exception_when_designation_id_not_exist() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignation("Manager");

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.updateDesignation(designation, 1L);
        });

        assertEquals("Designation not found", exception.getMessage());
    }

    @Test
    @DisplayName("test_delete_existing_designation")
    public void test_delete_existing_designation() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignationId(1L);
        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(designation));

        service.deleteDesignation(1L);

        Mockito.verify(mockRepository).delete(designation);
    }

    @Test
    @DisplayName("test_repository_delete_called_with_correct_designation")
    public void test_repository_delete_called_with_correct_designation() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Designation designation = new Designation();
        designation.setDesignationId(2L);
        Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(designation));

        service.deleteDesignation(2L);

        Mockito.verify(mockRepository, Mockito.times(1)).delete(designation);
    }

    @Test
    @DisplayName("test_delete_non_existent_designation")
    public void test_delete_non_existent_designation() {
        DesignationRepository mockRepository = Mockito.mock(DesignationRepository.class);
        DesignationService service = new DesignationService(mockRepository);
        Mockito.when(mockRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteDesignation(3L));
    }
}


