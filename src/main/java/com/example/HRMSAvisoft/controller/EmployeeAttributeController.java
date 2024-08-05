package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.service.EmployeeAttributeService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utils.ResponseGenerator;

import java.util.List;
@RestController
@RequestMapping("/api/v1/employeeAttribute")
public class EmployeeAttributeController
{
    private final ModelMapper modelMapper;

    private final EmployeeAttributeService employeeAttributeService;

    EmployeeAttributeController(ModelMapper modelMapper, EmployeeAttributeService employeeAttributeService){
        this.modelMapper = modelMapper;
        this.employeeAttributeService = employeeAttributeService;
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_EMPLOYEE_ATTRIBUTES')")
    @GetMapping("")
    public ResponseEntity<List<EmployeeAttribute>> getEmployeeAttributes(){
        List<EmployeeAttribute> employeeAttributes = employeeAttributeService.getEmployeeAttributes();
        return ResponseEntity.status(HttpStatus.OK).body(employeeAttributes);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_EMPLOYEE_ATTRIBUTE')")
    @PostMapping("")
    public ResponseEntity<Object> saveEmployeeAttribute( @RequestBody EmployeeAttribute employeeAttribute) throws EmployeeAttributeService.EmployeeAttributeAlreadyExistsException, IllegalArgumentException {
        EmployeeAttribute employeeAttributeAdded = employeeAttributeService.addEmployeeAttribute(employeeAttribute);
        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"EmployeeAttribute is created successfully",employeeAttributeAdded);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_EMPLOYEE_ATTRIBUTE')")
    @PatchMapping("/{employeeAttributeId}")
    public ResponseEntity<Object> updateEmployeeAttribute( @RequestBody EmployeeAttribute employeeAttribute, @PathVariable Long employeeAttributeId) throws EntityNotFoundException, IllegalArgumentException, AttributeKeyDoesNotExistException {
        EmployeeAttribute updatedEmployeeAttribute = employeeAttributeService.updateEmployeeAttribute(employeeAttribute, employeeAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "EmployeeAttribute Updated successfully.",updatedEmployeeAttribute);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_EMPLOYEE_ATTRIBUTE')")
    @DeleteMapping("/{employeeAttributeId}")
    public ResponseEntity<Object> deleteEmployeeAttribute(@PathVariable Long employeeAttributeId) throws EntityNotFoundException
    {
        EmployeeAttribute deletedEmployeeAttribute=employeeAttributeService.deleteEmployeeAttribute(employeeAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"EmployeeAttribute is Deleted successfully",deletedEmployeeAttribute);
    }

    @ExceptionHandler({
            EmployeeAttributeService.EmployeeAttributeAlreadyExistsException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof EmployeeAttributeService.EmployeeAttributeAlreadyExistsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
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


