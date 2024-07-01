package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmergencyContactRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmergencyContactController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = TestSecurityConfig.class)
public class EmergencyContactControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmergencyContactService emergencyContactService;

    @MockBean
    EmergencyContactRepository emergencyContactRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Value("${offset.getEmergencyContacts.url}")
    String getEmergencyContactsUrl;

    @Value("${offset.addEmergencyContact.url}")
    String addEmergencyContactUrl;

    @Value("${deleteEmergencyContact.url}")
    String deleteEmergencyContactUrl;

    private String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    @Test
    @DisplayName("test_getEmergencyContactsOfEmployee")
    public void testGetEmergencyContactsOfEmployee() throws Exception {
        List<EmergencyContact> emergencyContactList = new ArrayList<>();
        EmergencyContact emergencyContact1 = new EmergencyContact(1L, "208520952094", "brother");
        EmergencyContact emergencyContact2 = new EmergencyContact(2L, "208520952094", "father");
        emergencyContactList.add(emergencyContact1);
        emergencyContactList.add(emergencyContact2);

        when(emergencyContactService.getEmergencyContactsOfEmployee(anyLong())).thenReturn(emergencyContactList);
        mockMvc.perform(get(getEmergencyContactsUrl, 2L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


    @Test
    @DisplayName("test_addEmergencyContactToEmployee")
    void testAddEmergencyContactToEmployee() throws Exception {
        String jsonPayload = readFileAsString("src/test/resources/payloads/CreateEmergencyContact.json");

        // Mocking the EmployeeRepository to return an employee when findById is called
        Employee mockEmployee = new Employee();
        Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.of(mockEmployee));


        EmergencyContact mockEmergencyContact = new EmergencyContact();
        Mockito.when(emergencyContactService.addEmergencyContact(any(CreateEmergencyContactDTO.class), eq(2L)))
                .thenReturn(mockEmergencyContact);

        this.mockMvc.perform(post(addEmergencyContactUrl, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("test_delete_emergency_contact")
    void testDeleteEmergencyContact() throws Exception{
        Employee mockEmployee = mock(Employee.class);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(mockEmployee));

        when(emergencyContactRepository.findById(2L)).thenReturn(Optional.of(new EmergencyContact()));
        System.out.println(deleteEmergencyContactUrl);
        this.mockMvc.perform(delete(deleteEmergencyContactUrl, 2L, 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}

