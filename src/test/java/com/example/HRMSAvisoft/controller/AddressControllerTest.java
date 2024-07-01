package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.AddressType;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AddressControllerTest {
    private int port;

    private HttpClient client;
    AddressDTO addressDTO;
    JsonReader jsonReader =new JsonReader();
    Map<String,Object> dataMap=jsonReader.readFile("address");

        String propertyNumber=(String)dataMap.get("propertyNumber");
        AddressType addressType=(AddressType)dataMap.get("addressType");
        Long zipCode=Long.valueOf(dataMap.get("zipCode").toString());
        String city=(String)dataMap.get("city");
        String state=(String)dataMap.get("state");
        String country=(String)dataMap.get("country");

    public AddressControllerTest() throws IOException {
    }

    @BeforeEach
    public void setUp(){
        port=5555;
        client = HttpClient.newHttpClient();
        addressDTO=new AddressDTO();
        addressDTO.setPropertyNumber(propertyNumber);
        addressDTO.setAddressType(addressType);
        addressDTO.setZipCode(zipCode);
        addressDTO.setCity(city);
        addressDTO.setState(state);
        addressDTO.setCountry(country);

    }
    @Test
    @DisplayName("AddAddressToEmployee")
    @Transactional
    public void addAddressToEmployee() throws IOException, InterruptedException {
        Long employeeId = 3L;
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/addNewAddress";

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(addressDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());


    }
    @Test
    @Transactional
    @DisplayName("Remove Address from Employee")
    public void removeAddressFromEmployee() throws IOException, InterruptedException {
        Long employeeId = 3L;
        Long addressId = 23L; // Assuming addressId
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/removeAddress/" + addressId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        // Add more assertions for the response body if needed
    }
    @Test
    @DisplayName("Edit Address")
    @Transactional
    public void editAddress() throws IOException, InterruptedException {
        Long employeeId = 4L;
        Long addressId = 9L; //
        String url = "http://localhost:" + port + "/api/v1/address/" + employeeId + "/editAddress/" + addressId;


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(addressDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }


}
