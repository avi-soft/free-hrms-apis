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
import jakarta.validation.ValidationException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB in bytes
    private static final List<String> ACCEPTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

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
    public ResponseEntity<String> uploadProfileImage(@PathVariable("employeeId") Long employeeId, @RequestParam("file") MultipartFile file) throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException ,AccessDeniedException, ValidationException{
        employeeService.uploadProfileImage(employeeId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
    }

    // Validation method for file
    private void validateImage(MultipartFile file) throws ValidationException {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new ValidationException("File is empty. Please upload a valid image.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException(file.getSize());
        }

        // Check file content type (e.g., only JPEG, PNG)
        if (!ACCEPTED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ValidationException("Invalid file type. Only JPEG and PNG files are allowed.");
        }
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
            loginUserResponseDTO.setFirstName(employee.getFirstName());
            loginUserResponseDTO.setLastName(employee.getLastName());
//            loginUserResponseDTO.setSalary(employee.getSalary());
//            loginUserResponseDTO.setJoinDate(employee.getJoinDate());
//            loginUserResponseDTO.setAdhaarNumber(employee.getAdhaarNumber());
//            loginUserResponseDTO.setDateOfBirth(employee.getDateOfBirth());
//            loginUserResponseDTO.setPosition(employee.getPosition());
            loginUserResponseDTO.setProfileImage(employee.getProfileImage());
            loginUserResponseDTO.setGender(employee.getGender());
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
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "noSort") String sortBy) throws AccessDeniedException {

        Page<Employee> employeesPage = employeeService.getAllEmployees(page, size, sortBy);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Employees fetched successfully", "Employees", employeesPage));
    }

    @PreAuthorize("hasAuthority('FIND_EMPLOYEE_BY_ID')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<Map<String,Object>> getEmployeeById(@PathVariable Long employeeId) throws NullPointerException, EmployeeNotFoundException, DataAccessException, AccessDeniedException {
        EmployeeWithOrganizationResponseDTO employeeWithOrganizationResponseDTO= employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok().body(Map.of("Employee", employeeWithOrganizationResponseDTO, "message", "Employee retrieved Successfully", "Status", true));
    }

    @GetMapping("/unassignedEmployeesOfDepartment")
    public ResponseEntity<Map<String, Object>> getAllUnassignedEmployeesOfDepartment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Employee> unassignedEmployeesList = employeeService.getUnassignedEmployeesOfDepartment(page, size);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Employees fetched successfully", "Employees", unassignedEmployeesList));
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
            EntityNotFoundException.class,
            MaxUploadSizeExceededException.class,
            ValidationException.class

    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException)
        {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof MaxUploadSizeExceededException) {
            message = "File size should be less than 5Mb";
            status = HttpStatus.PAYLOAD_TOO_LARGE;
        }
        else if (exception instanceof ValidationException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
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
