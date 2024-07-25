package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.dto.DepartmentsResponseDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.DepartmentService;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/department")
@Transactional
public class DepartmentController {

    private final DepartmentService departmentService;

    DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAuthority('GETALL_DEPARTMENTS')")
    @GetMapping("/{organizationId}")
    public ResponseEntity<List<DepartmentsResponseDTO>> getAllDepartments(@PathVariable("organizationId") Long organizationId) {
        List<Department> departments = departmentService.getAllDepartments(organizationId);
        List<DepartmentsResponseDTO> departmentsResponseDTOS = departments.stream().map(department ->{
            DepartmentsResponseDTO departmentsResponseDTO = new DepartmentsResponseDTO();
            departmentsResponseDTO.setDepartmentId(department.getDepartmentId());
            departmentsResponseDTO.setDescription(department.getDescription());
            departmentsResponseDTO.setDepartment(department.getDepartment());
            departmentsResponseDTO.setManagerId(department.getManager().getEmployeeId());
            departmentsResponseDTO.setManagerEmployeeCode(department.getManager().getEmployeeCode());
            departmentsResponseDTO.setManagerFirstName(department.getManager().getFirstName());
            departmentsResponseDTO.setManagerLastName(department.getManager().getLastName());
            departmentsResponseDTO.setOrganizationId(department.getOrganization().getOrganizationId());
            departmentsResponseDTO.setOrganizationName(department.getOrganization().getOrganizationName());
            departmentsResponseDTO.setOrganizationDescription(department.getOrganization().getOrganizationDescription());
            departmentsResponseDTO.setOrganizationImage(department.getOrganization().getOrganizationImage());
            return departmentsResponseDTO;
        }).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(departmentsResponseDTOS);
    }

    @PostMapping("/{organizationId}")
    @PreAuthorize("hasAuthority('ADD_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> addDepartment(@Valid @RequestBody CreateDepartmentDTO createDepartmentDTO, @PathVariable("organizationId") Long organizationId) throws EmployeeNotFoundException, EntityNotFoundException, DepartmentService.DepartmentAlreadyExistsException {
        Department createdDepartment = departmentService.addDepartment(createDepartmentDTO, organizationId);
        DepartmentsResponseDTO departmentsResponseDTO = new DepartmentsResponseDTO();
        departmentsResponseDTO.setDepartmentId(createdDepartment.getDepartmentId());
        departmentsResponseDTO.setDescription(createdDepartment.getDescription());
        departmentsResponseDTO.setDepartment(createdDepartment.getDepartment());
        departmentsResponseDTO.setManagerId(createdDepartment.getManager().getEmployeeId());
        departmentsResponseDTO.setManagerEmployeeCode(createdDepartment.getManager().getEmployeeCode());
        departmentsResponseDTO.setManagerFirstName(createdDepartment.getManager().getFirstName());
        departmentsResponseDTO.setManagerLastName(createdDepartment.getManager().getLastName());
        departmentsResponseDTO.setOrganizationId(createdDepartment.getOrganization().getOrganizationId());
        departmentsResponseDTO.setOrganizationName(createdDepartment.getOrganization().getOrganizationName());
        departmentsResponseDTO.setOrganizationDescription(createdDepartment.getOrganization().getOrganizationDescription());
        departmentsResponseDTO.setOrganizationImage(createdDepartment.getOrganization().getOrganizationImage());
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Department created successfully", "Department", departmentsResponseDTO));
    }

    @PatchMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> updateDepartment(@Valid @RequestBody CreateDepartmentDTO createDepartmentDTO, @PathVariable("departmentId") Long departmentId) throws EmployeeNotFoundException, DepartmentService.DepartmentNotFoundException, DepartmentService.DepartmentAlreadyExistsException {
        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, departmentId);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Department updated successfully"));
    }

    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT')")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable("departmentId") Long departmentId)throws DepartmentService.DepartmentNotFoundException {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Department deleted successfully"));
    }

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            DepartmentService.DepartmentNotFoundException.class,
            DepartmentService.DepartmentNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof DepartmentService.DepartmentAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
       else if (exception instanceof DepartmentService.DepartmentNotFoundException){
           message = exception.getMessage();
           status = HttpStatus.NOT_FOUND;
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
