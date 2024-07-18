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

            return departmentsResponseDTO;
        }).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(departmentsResponseDTOS);
    }

    @PostMapping("/{organizationId}")
    @PreAuthorize("hasAuthority('ADD_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> addDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, @PathVariable("organizationId") Long organizationId) throws EmployeeNotFoundException, EntityNotFoundException {
        Department createdDepartment = departmentService.addDepartment(createDepartmentDTO, organizationId);
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Department created successfully", "Department", createdDepartment));
    }

    @PatchMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> updateDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, @PathVariable("departmentId") Long departmentId) throws EmployeeNotFoundException, DepartmentService.DepartmentNotFoundException {
        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, departmentId);
        return ResponseEntity.status(204).body(Map.of("success", true, "message", "Department updated successfully", "Department", updatedDepartment));
    }

    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT')")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable("departmentId") Long departmentId)throws DepartmentService.DepartmentNotFoundException {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(204).body(Map.of("success", true, "message", "Department deleted successfully"));
    }

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            DepartmentService.DepartmentNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
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
