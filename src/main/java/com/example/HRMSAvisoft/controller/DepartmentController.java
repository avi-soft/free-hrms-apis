package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.dto.DepartmentsResponseDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.DepartmentAttributeService;
import com.example.HRMSAvisoft.service.DepartmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("")
    public ResponseEntity<Page<DepartmentsResponseDTO>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

            Page<Department> departmentsPage = departmentService.getAllDepartments(page, size);
            Page<DepartmentsResponseDTO> departmentsResponseDTOSPage = departmentsPage.map(department ->{
            DepartmentsResponseDTO departmentsResponseDTO = new DepartmentsResponseDTO();
            departmentsResponseDTO.setDepartmentId(department.getDepartmentId());
            departmentsResponseDTO.setDescription(department.getDescription());
            departmentsResponseDTO.setDepartment(department.getDepartment());
            departmentsResponseDTO.setManagerId(department.getManager().getEmployeeId());
            departmentsResponseDTO.setManagerEmployeeCode(department.getManager().getEmployeeCode());
            departmentsResponseDTO.setManagerFirstName(department.getManager().getFirstName());
            departmentsResponseDTO.setManagerLastName(department.getManager().getLastName());
            departmentsResponseDTO.setAttributes(department.getAttributes());
            return departmentsResponseDTO;
        });

        return ResponseEntity.ok(departmentsResponseDTOSPage);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADD_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> addDepartment(@Valid @RequestBody CreateDepartmentDTO createDepartmentDTO) throws EmployeeNotFoundException, EntityNotFoundException, DepartmentAttributeService.DepartmentAlreadyExistsException, AttributeKeyDoesNotExistException {
        Department createdDepartment = departmentService.addDepartment(createDepartmentDTO);
        DepartmentsResponseDTO departmentsResponseDTO = new DepartmentsResponseDTO();
        departmentsResponseDTO.setDepartmentId(createdDepartment.getDepartmentId());
        departmentsResponseDTO.setDescription(createdDepartment.getDescription());
        departmentsResponseDTO.setDepartment(createdDepartment.getDepartment());
        departmentsResponseDTO.setManagerId(createdDepartment.getManager().getEmployeeId());
        departmentsResponseDTO.setManagerEmployeeCode(createdDepartment.getManager().getEmployeeCode());
        departmentsResponseDTO.setManagerFirstName(createdDepartment.getManager().getFirstName());
        departmentsResponseDTO.setManagerLastName(createdDepartment.getManager().getLastName());
        departmentsResponseDTO.setAttributes(createdDepartment.getAttributes());
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Department created successfully", "Department", departmentsResponseDTO));
    }

    @PatchMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('UPDATE_DEPARTMENT')")
    public ResponseEntity<Map<String,Object>> updateDepartment(@Valid @RequestBody CreateDepartmentDTO createDepartmentDTO, @PathVariable("departmentId") Long departmentId) throws EmployeeNotFoundException, DepartmentService.DepartmentNotFoundException, DepartmentAttributeService.DepartmentAlreadyExistsException {
        Department updatedDepartment = departmentService.updateDepartment(createDepartmentDTO, departmentId);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Department updated successfully"));
    }

    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT')")
    public ResponseEntity<Map<String, Object>> deleteDepartment(@PathVariable("departmentId") Long departmentId)throws DepartmentService.DepartmentNotFoundException {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Department deleted successfully"));
    }

    @PatchMapping("/{organizationId}/assignDepartment/{departmentId}")
    public ResponseEntity<Map<String, Object>> assignDepartmentToOrganization(@PathVariable("organizationId") Long organizationId, @PathVariable("departmentId") Long departmentId){
        departmentService.assignDepartmentToOrganization(organizationId, departmentId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Department assigned successfully"));
    }

    @PatchMapping("/{organizationId}/removeDepartment/{departmentId}")
    public ResponseEntity<Map<String, Object>> removeDepartmentFromOrganization(@PathVariable("organizationId") Long organizationId,@PathVariable("departmentId") Long departmentId){
        departmentService.removeDepartmentFromOrganization(organizationId, departmentId);
        return ResponseEntity.status(200).body(Map.of("message", "Department removed successfully", "success", true));
    }


    @ExceptionHandler({
            EmployeeNotFoundException.class,
            DepartmentService.DepartmentNotFoundException.class,
            AttributeKeyDoesNotExistException.class,
            DepartmentAttributeService.DepartmentAlreadyExistsException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof DepartmentAttributeService.DepartmentAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof AttributeKeyDoesNotExistException){
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

