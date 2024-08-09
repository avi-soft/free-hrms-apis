package com.example.HRMSAvisoft.controller;


import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.service.DepartmentAttributeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utils.ResponseGenerator;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departmentAttribute")
public class DepartmentAttributeController {

    private final DepartmentAttributeService departmentAttributeService;

    DepartmentAttributeController(DepartmentAttributeService departmentAttributeService) {
        this.departmentAttributeService = departmentAttributeService;
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_DEPARTMENT_ATTRIBUTES')")
    @GetMapping("")
    public ResponseEntity<List<DepartmentAttribute>> getDepartmentAttributes(){
        List<DepartmentAttribute> departmentAttributes = departmentAttributeService.getDepartmentAttributes();
        return ResponseEntity.status(HttpStatus.OK).body(departmentAttributes);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_DEPARTMENT_ATTRIBUTE')")
    @PostMapping("")
    public ResponseEntity<Object> saveDepartmentAttribute( @RequestBody DepartmentAttribute departmentAttribute) throws DepartmentAttributeService.DepartmentAlreadyExistsException, IllegalArgumentException {
        DepartmentAttribute newDepartmentAttribute = departmentAttributeService.addDepartmentAttribute(departmentAttribute);
        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Department Attribute is created successfully",newDepartmentAttribute);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_DEPARTMENT_ATTRIBUTE')")
    @PatchMapping("/{departmentAttributeId}")
    public ResponseEntity<Object> updateDepartmentAttribute( @RequestBody DepartmentAttribute departmentAttribute, @PathVariable Long departmentAttributeId) throws EntityNotFoundException, IllegalArgumentException, DepartmentAttributeService.DepartmentAlreadyExistsException {
        DepartmentAttribute updatedDepartmentAttribute = departmentAttributeService.updateDepartmentAttribute(departmentAttribute, departmentAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "Department Attribute Updated successfully.",updatedDepartmentAttribute);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_DEPARTMENT_ATTRIBUTE')")
    @DeleteMapping("/{departmentAttributeId}")
    public ResponseEntity<Object> deleteDepartmentAttribute(@PathVariable Long departmentAttributeId) throws EntityNotFoundException
    {
        DepartmentAttribute deletedDepartmentAttribute =departmentAttributeService.deleteDepartmentAttribute(departmentAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"Department Attribute is Deleted successfully",deletedDepartmentAttribute);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            DepartmentAttributeService.DepartmentAlreadyExistsException.class,
            EntityNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof DepartmentAttributeService.DepartmentAlreadyExistsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else {
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
