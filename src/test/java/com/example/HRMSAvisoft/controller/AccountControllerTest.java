package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.config.TestSecurityConfig;
import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = TestSecurityConfig.class)

public class AccountControllerTest {

    @MockBean
    AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    HttpClient client;
    String port;
    AddAccountDTO accountDTO;
    JsonReader jsonReader =new JsonReader();
    Map<String,Object>dataMap=jsonReader.readFile("account");
    String accountNumber=(String) dataMap.get("accountNumber");
    String ifsc=(String) dataMap.get("ifsc");
    String bankName=(String) dataMap.get("bankName");
    String branch=(String)dataMap.get("branch");

    public AccountControllerTest() throws IOException {
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        accountDTO=new AddAccountDTO();
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setIfsc(ifsc);
        accountDTO.setBankName(bankName);
        accountDTO.setBranch(branch);
        client =HttpClient.newHttpClient();
        port="5555";

    }

    @Test
    @WithMockUser
    public void addAccountTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        when(accountService.addAccountToEmployee(1L,accountDTO)).thenReturn(new Employee());
        mockMvc.perform(post("/api/v1/account/1/AddBankAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(accountDTO)))
                .andExpect(status().isCreated());

    }
    @Test
    @Transactional
    @DisplayName("AddAccount")
    public void addAccount() throws  IOException, InterruptedException,EmployeeNotFoundException {
        Long employeeId = 1L;
        String url = "http://localhost:5555/api/v1/account/2/AddBankAccount";



        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(accountDTO);

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the response status code
        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        System.out.println(responseBody);
    }

    @Test
    @Transactional
    @DisplayName("RemoveAccountFromEmployee")
    public void removeAccountFromEmployee_Success() throws IOException, InterruptedException , EmployeeNotFoundException {
        Long employeeId = 2L;
        String url = "http://localhost:5555/api/v1/account/" + employeeId + "/removeAccount";

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check the response status code and body
        assertEquals(200, response.statusCode());
        String responseBody = response.body();
        System.out.println(responseBody);
//        assertEquals("{\"success\":true,\"message\":\"Account Removed\"}", response.body());
    }

}
