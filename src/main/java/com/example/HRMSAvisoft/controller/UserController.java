package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.UserRepository;
import com.example.HRMSAvisoft.service.JWTService;
import com.example.HRMSAvisoft.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
@Transactional
public class UserController {


    private UserService userService;
    private JWTService jwtService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, JWTService jwtService, ModelMapper modelMapper)
    {
        this.userService=userService;
        this.jwtService=jwtService;
        this.modelMapper=modelMapper;
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello ";
    }


    @Operation(
            tags = "createUser",
            description = "Create a new user",
            responses = {
                    @ApiResponse(
                            description = "created",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "email already exists",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/addNewUser/{organizationId}")
    @PreAuthorize("hasAuthority('CREATE_NEW_USER')")
    public ResponseEntity<Map<String ,Object>>addNewUser(@AuthenticationPrincipal User loggedInUser,
                                                         @RequestBody @Valid AddNewUserDTO addNewUserDTO,@PathVariable Long organizationId)throws IOException,UserService.EmailAlreadyExistsException, EntityNotFoundException{
        User createdUser=userService.addNewUser(addNewUserDTO,loggedInUser,organizationId);
        NewUserResponseDTO newUser=new NewUserResponseDTO();
        newUser.setUserId(createdUser.getUserId());
        newUser.setEmail(createdUser.getEmail());
        newUser.setCreatedAt(createdUser.getCreatedAt());
        newUser.setEmployeeId(createdUser.getEmployee().getEmployeeId());
//        leaveBalanceService.initializeLeaveBalancesForEmployee(createdUser.getEmployee());
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "New User Created!!");
        response.put("success", true);
        response.put("newUser", newUser);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody LoginUserDTO loginUserDTO)throws EntityNotFoundException, IllegalAccessException, IllegalArgumentException, UserService.WrongPasswordCredentialsException {
        User loggedInUser = userService.userLogin(loginUserDTO);

        String token = null;

        LoginUserResponseDTO userResponse = new LoginUserResponseDTO();
        if(loggedInUser!=null) {
            userResponse.setUserId(loggedInUser.getUserId());
//            userResponse.setEmail(loggedInUser.getEmail());
            userResponse.setRoles(loggedInUser.getRoles());
            userResponse.setCreatedAt(loggedInUser.getCreatedAt());
            Employee employee = loggedInUser.getEmployee();
            userResponse.setEmployeeId(employee.getEmployeeId());
            userResponse.setFirstName(employee.getFirstName());
            userResponse.setLastName(employee.getLastName());
            userResponse.setActive(loggedInUser.getActive());
//            userResponse.setContact(employee.getContact());
            userResponse.setDepartments(employee.getDepartments());
            userResponse.setEmployeeCode(employee.getEmployeeCode());
//            userResponse.setAdhaarNumber(employee.getAdhaarNumber());
//            userResponse.setPanNumber(employee.getPanNumber());
//            userResponse.setUanNumber(employee.getUanNumber());
            userResponse.setAddresses(employee.getAddresses());
//            userResponse.setPosition(employee.getPosition());
//            userResponse.setJoinDate(employee.getJoinDate());
//            userResponse.setGender(employee.getGender());
            String userProfileImage = userResponse.getProfileImage() == null ? "https://api.dicebear.com/5.x/initials/svg?seed="+userResponse.getFirstName()+" "+userResponse.getLastName() : userResponse.getProfileImage();
            userResponse.setProfileImage(userProfileImage);
//            userResponse.setDateOfBirth(employee.getDateOfBirth());
//            userResponse.setAccount(employee.getAccount());
//            userResponse.setSalary(employee.getSalary());

            //genereate token
            token = JWTService.createJWT(loggedInUser.getUserId(), loggedInUser.getRoles());

        }



        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "Login Successful");
        response.put("success", true);
        response.put("token", token);
        response.put("loginUser", userResponse);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteEmployee(@PathVariable("userId") Long userId)throws EmployeeNotFoundException {
            userService.deleteUser(userId);
            return ResponseEntity.status(204).body(null);
    }

    @PreAuthorize("hasAuthority('GET_ALL_USERS')")
    @GetMapping("/getAllUserInfo")
    public ResponseEntity<Map<String, Object>> getAllUserInfo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "noSort") String sortBy) {

        Page<UserInfoDTO> pageOfUsers = userService.getAllUserInfo(page, size, sortBy);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("Users", pageOfUsers.getContent());
        responseData.put("currentPage", pageOfUsers.getNumber());
        responseData.put("totalItems", pageOfUsers.getTotalElements());
        responseData.put("totalPages", pageOfUsers.getTotalPages());
        responseData.put("message", "User Information Retrieved Successfully");
        responseData.put("Success", true);

        return ResponseEntity.ok().body(responseData);
    }

    @PatchMapping("/setActiveStatus/{userId}")
    public ResponseEntity<Map<String, Object>> setActiveStatus(@RequestParam Boolean activeStatus, @PathVariable("userId") Long userId){
        userService.setActiveStatus(activeStatus, userId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Active status set"));
    }



//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
//        // Extract the token from the Authorization header
//        String jwt = token.substring(7); // Assuming "Bearer " prefix is used
//
//        // Add the token to the blacklist
//        jwtBlacklist.addToBlacklist(jwt);
//
//        // You can also implement additional logic here, such as notifying clients of successful logout
//
//        return ResponseEntity.ok("Logout successful");
//    }


    @ExceptionHandler(
            {UserService.WrongPasswordCredentialsException.class
                    ,UserService.EmailAlreadyExistsException.class,
                    IOException.class,
                    IllegalArgumentException.class,
                    EntityNotFoundException.class,
                    IllegalAccessException.class
            })
    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof IllegalArgumentException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof IllegalAccessException){
            message = exception.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else if(exception instanceof UserService.WrongPasswordCredentialsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof IOException){
            message = "Failed to upload Profile Image";
            status = HttpStatus.FAILED_DEPENDENCY;
        }
        else if (exception instanceof UserService.EmailAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}