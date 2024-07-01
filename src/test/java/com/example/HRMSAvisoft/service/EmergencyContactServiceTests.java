package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmergencyContactRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmergencyContactServiceTests {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmergencyContactService emergencyContactService;

    @Mock
    EmergencyContactRepository emergencyContactRepository;

    @Test
    @DisplayName("test_getEmergencyContactsForValidEmployeeId")
    public void test_returns_emergency_contacts_for_valid_employee_id()throws EmployeeNotFoundException{
        Long employeeId = 1L;
        Employee employee = new Employee();
        List<EmergencyContact> emergencyContacts = new ArrayList<>();
        emergencyContacts.add(new EmergencyContact());
        employee.setEmergencyContacts(emergencyContacts);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        List<EmergencyContact> result = emergencyContactService.getEmergencyContactsOfEmployee(employeeId);

        assertEquals(emergencyContacts, result);
    }

    @Test
    @DisplayName("test_addEmergencyContactSuccess")
    public void test_addEmergencyContact_success()throws EmployeeNotFoundException {
        Long employeeId = 1L;
        CreateEmergencyContactDTO createEmergencyContactDTO = new CreateEmergencyContactDTO();
        createEmergencyContactDTO.setContact("kirandeep");
        createEmergencyContactDTO.setRelationship("Friend");

        EmergencyContact emergencyContact = new EmergencyContact(1L, createEmergencyContactDTO.getContact(), createEmergencyContactDTO.getRelationship());
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(emergencyContactRepository.save(any(EmergencyContact.class))).thenReturn(emergencyContact);
        EmergencyContact result = emergencyContactService.addEmergencyContact(createEmergencyContactDTO, employeeId);

        assertNotNull(result);
        assertEquals(createEmergencyContactDTO.getContact(), result.getContact());
        assertEquals(createEmergencyContactDTO.getRelationship(), result.getRelationship());
        assertTrue(employee.getEmergencyContacts().contains(result));
    }

    @Test
    @DisplayName("test_updateEmergencyContactSuccess")
    public void test_updateEmergencyContact_ReturnsUpdatedEmergencyContact() throws Exception{
        Long emergencyContactId = 1L;
        CreateEmergencyContactDTO createEmergencyContactDTO = new CreateEmergencyContactDTO();
        createEmergencyContactDTO.setContact("John Doe");
        createEmergencyContactDTO.setRelationship("Friend");
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setEmergencyContactId(emergencyContactId);
        emergencyContact.setContact("Jane Doe");
        emergencyContact.setRelationship("Sibling");

        when(emergencyContactRepository.findById(emergencyContactId)).thenReturn(Optional.of(emergencyContact));
        when(emergencyContactRepository.save(any(EmergencyContact.class))).thenReturn((emergencyContact));

        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(createEmergencyContactDTO, emergencyContactId);

        assertEquals(updatedEmergencyContact, emergencyContact);
    }
}
