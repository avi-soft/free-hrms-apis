package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.config.GlobalExceptionHandler;
import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.entity.Employee;
//import com.example.HRMSAvisoft.entity.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.Skill;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeAttributeRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeAttributeRepository employeeAttributeRepository;

    public EmployeeController(EmployeeService employeeService){

        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAuthority('UPLOAD_EMPLOYEE_IMAGE')")
    @PostMapping("/{employeeId}/uploadImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable("employeeId") Long employeeId, @RequestParam("file") MultipartFile file) throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException ,AccessDeniedException{
        employeeService.uploadProfileImage(employeeId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
    }

    @PreAuthorize("hasAuthority('SEARCH_EMPLOYEE_BY_NAME')")
    @GetMapping("/searchEmployee")
    public ResponseEntity<List<LoginUserResponseDTO>> searchEmployeesByName(@RequestParam("name") String name)throws IllegalArgumentException,AccessDeniedException{
        List<Employee> searchedEmployees = employeeService.searchEmployeesByName(name);
        List<LoginUserResponseDTO> loginUserResponseDTOs = searchedEmployees.stream().map((employee)->{
            LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO();
            loginUserResponseDTO.setEmployeeCode(employee.getEmployeeCode());
//            if(employee.getDepartment() != null) {
//                loginUserResponseDTO.setDepartment(employee.getDepartment().getDepartment());
//                loginUserResponseDTO.setDepartmentId(employee.getDepartment().getDepartmentId());
//                loginUserResponseDTO.setDepartmentDescription(employee.getDepartment().getDescription());
//                loginUserResponseDTO.setManagerId(employee.getDepartment().getManager().getEmployeeId());
//            }
            loginUserResponseDTO.setEmployeeId(employee.getEmployeeId());
            loginUserResponseDTO.setAddresses(employee.getAddresses());
            loginUserResponseDTO.setAttributes(employee.getAttributes());
//            loginUserResponseDTO.setAccount(employee.getAccount());
//            loginUserResponseDTO.setUanNumber(employee.getUanNumber());
//            loginUserResponseDTO.setPanNumber(employee.getPanNumber());
//            loginUserResponseDTO.setContact(employee.getContact());
//            loginUserResponseDTO.setFirstName(employee.getFirstName());
//            loginUserResponseDTO.setLastName(employee.getLastName());
//            loginUserResponseDTO.setSalary(employee.getSalary());
//            loginUserResponseDTO.setJoinDate(employee.getJoinDate());
//            loginUserResponseDTO.setAdhaarNumber(employee.getAdhaarNumber());
//            loginUserResponseDTO.setDateOfBirth(employee.getDateOfBirth());
//            loginUserResponseDTO.setPosition(employee.getPosition());
            loginUserResponseDTO.setProfileImage(employee.getProfileImage());
//            loginUserResponseDTO.setGender(employee.getGender());
            User userEmployee = userRepository.findByEmployee(employee);
            loginUserResponseDTO.setUserId(userEmployee.getUserId());
//            loginUserResponseDTO.setEmail(userEmployee.getEmail());
            loginUserResponseDTO.setRoles(userEmployee.getRoles());
            loginUserResponseDTO.setCreatedAt(userEmployee.getCreatedAt());
            return loginUserResponseDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(loginUserResponseDTOs);
    }

//    @PreAuthorize("hasAuthority('SEARCH_EMPLOYEE_BY_MANAGER_ID')")
//    @GetMapping("/searchByManager")
//    public ResponseEntity<List<LoginUserResponseDTO>> searchEmployeeByManagerId(@RequestParam("managerId") Long managerId) throws IllegalArgumentException, AccessDeniedException {
//        List<Employee> searchedEmployees = employeeService.searchEmployeeByManagerId(managerId);
//        List<LoginUserResponseDTO> loginUserResponseDTOs = searchedEmployees.stream().map((employee)->{
//            LoginUserResponseDTO loginUserResponseDTO = new LoginUserResponseDTO();
//            loginUserResponseDTO.setEmployeeCode(employee.getEmployeeCode());
////            if(employee.getDepartment() != null) {
////                loginUserResponseDTO.setDepartment(employee.getDepartment().getDepartment());
////                loginUserResponseDTO.setDepartmentId(employee.getDepartment().getDepartmentId());
////                loginUserResponseDTO.setDepartmentDescription(employee.getDepartment().getDescription());
////                loginUserResponseDTO.setManagerId(employee.getDepartment().getManager().getEmployeeId());
////            }
//            loginUserResponseDTO.setEmployeeId(employee.getEmployeeId());
//            loginUserResponseDTO.setAddresses(employee.getAddresses());
////            loginUserResponseDTO.setAccount(employee.getAccount());
////            loginUserResponseDTO.setUanNumber(employee.getUanNumber());
////            loginUserResponseDTO.setPanNumber(employee.getPanNumber());
////            loginUserResponseDTO.setContact(employee.getContact());
//            loginUserResponseDTO.setFirstName(employee.getFirstName());
//            loginUserResponseDTO.setLastName(employee.getLastName());
////            loginUserResponseDTO.setSalary(employee.getSalary());
////            loginUserResponseDTO.setJoinDate(employee.getJoinDate());
////            loginUserResponseDTO.setAdhaarNumber(employee.getAdhaarNumber());
////            loginUserResponseDTO.setDateOfBirth(employee.getDateOfBirth());
////            loginUserResponseDTO.setPosition(employee.getPosition());
//            loginUserResponseDTO.setProfileImage(employee.getProfileImage());
//            loginUserResponseDTO.setAttributes(employee.getAttributes());
////            loginUserResponseDTO.setGender(employee.getGender());
//            User userEmployee = userRepository.findByEmployee(employee);
//            loginUserResponseDTO.setUserId(userEmployee.getUserId());
////            loginUserResponseDTO.setEmail(userEmployee.getEmail());
//            loginUserResponseDTO.setRoles(userEmployee.getRoles());
//            loginUserResponseDTO.setCreatedAt(userEmployee.getCreatedAt());
//            return loginUserResponseDTO;
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.ok(loginUserResponseDTOs);
//    }

    @PreAuthorize("hasAuthority('ADD_EMPLOYEE')")
    @PostMapping("/{employeeId}")
    public ResponseEntity<Map<String, Object>> saveEmployeePersonalInfo(@PathVariable Long employeeId, @RequestBody  @Valid CreateEmployeeDTO createEmployee) throws EmployeeNotFoundException, AttributeKeyDoesNotExistException, EmployeeService.EmployeeCodeAlreadyExistsException, AccessDeniedException {
        Employee newEmployee = employeeService.saveEmployeePersonalInfo(employeeId, createEmployee);
        return ResponseEntity.ok(Map.of("success", true, "message", "Employee created Successfully", "Employee", newEmployee));
    }

  @PreAuthorize("hasAuthority('GET_ALL_EMPLOYEES')")
  @GetMapping("/getAllEmployees")
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "employeeId") String sortBy) throws AccessDeniedException {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Employee> pageOfEmployees = employeeService.getAllEmployees(pageable);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("Employees", pageOfEmployees.getContent());
        responseData.put("currentPage", pageOfEmployees.getNumber());
        responseData.put("totalItems", pageOfEmployees.getTotalElements());
        responseData.put("totalPages", pageOfEmployees.getTotalPages());
        responseData.put("message", "Employees Retrieved Successfully");
        responseData.put("Success", true);

        return ResponseEntity.ok().body(responseData);
    }

    @PreAuthorize("hasAuthority('FIND_EMPLOYEE_BY_ID')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable Long employeeId) throws NullPointerException, EmployeeNotFoundException, DataAccessException, AccessDeniedException {
        Employee employee= employeeService.getEmployeeById(employeeId);
        Map<String, Object> responseData = new HashMap<>();
        return ResponseEntity.ok().body(Map.of("Employee", employee, "message", "Employee retrieved Successfully", "Status", true));
    }

    @PreAuthorize("hasAuthority('UPDATE_EMPLOYEE_COMPANY_DETAILS')")
    @PatchMapping(value = "/updateEmployeeDetails/{employeeId}")
    public ResponseEntity<Map<String,Object>>updateEmployeeDetails(@PathVariable Long employeeId, @RequestBody UpdateEmployeeDetailsDTO updateEmployeeDetailsDTO) throws NullPointerException, EmployeeNotFoundException, AccessDeniedException ,AttributeKeyDoesNotExistException{
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, updateEmployeeDetailsDTO);
        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",updatedEmployee , "message", "Personal Details Updated", "success", true));
    }

    @ExceptionHandler({
            IOException.class,
            RuntimeException.class,
            IllegalArgumentException.class,
            EmployeeService.EmployeeCodeAlreadyExistsException.class,
            AttributeKeyDoesNotExistException.class,
            EntityNotFoundException.class

    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException)
        {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof EmployeeService.EmployeeCodeAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof AttributeKeyDoesNotExistException){
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof NullPointerException) {
            message = exception.getMessage();
            status =  HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof IOException) {
            message = "Failed to update Profile Image";
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof org.springframework.security.access.AccessDeniedException)
        {
            message="Acess is denied for current user";
            status = HttpStatus.FORBIDDEN;
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
