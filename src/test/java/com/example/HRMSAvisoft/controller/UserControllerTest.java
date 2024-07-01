package com.example.HRMSAvisoft.controller;


import com.example.HRMSAvisoft.dto.CreateUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JWTService jwtService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    @DisplayName("Test Save User")
    public void testSaveUser() throws Exception {
        // Create a mock user
        Employee mockEmployee = new Employee();
        mockEmployee.setEmployeeId(1L);
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");

        when(userService.saveUser(any(CreateUserDTO.class), any(User.class))).thenReturn(mockEmployee);
        // Perform POST request to "/api/v1/user/saveUser" with JSON request body
        mockMvc.perform(post("/api/v1/user/saveUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User Created Successfully"))
                .andExpect(jsonPath("$.employeeId").value(1L));
    }

    @Test
    @DisplayName("Test User Login")
    void testUserLogin() throws Exception {
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setEmail("testuser");
        loginUserDTO.setPassword("testpassword");

        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail("test@example.com");

        Employee mockEmployee = new Employee();
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");
        mockUser.setEmployee(mockEmployee);

        // Configure UserService mock
        when(userService.userLogin(any(LoginUserDTO.class))).thenReturn(mockUser);

        // Perform POST request and verify response
        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserDTO)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.message").value("Login Successful"))
                .andExpect(jsonPath("$.loginUser.userId").value(1L))
                .andExpect(jsonPath("$.loginUser.email").value("test@example.com"))
                .andExpect(jsonPath("$.loginUser.firstName").value("John"))
                .andExpect(jsonPath("$.loginUser.lastName").value("Doe"));
    }



}



