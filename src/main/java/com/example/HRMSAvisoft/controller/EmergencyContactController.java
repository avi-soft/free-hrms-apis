package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreateEmergencyContactDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.EmergencyContact;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.EmergencyContactService;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emergencyContact")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    public ResponseEntity<Map<String, Object>> getEmergencyContactsOfEmployee(@PathVariable("employeeId") Long employeeId) throws EmployeeNotFoundException {
        List<EmergencyContact> emergencyContactsOfEmployee = emergencyContactService.getEmergencyContactsOfEmployee(employeeId);
        if (emergencyContactsOfEmployee.isEmpty()){
            return ResponseEntity.ok(Map.of("success", true, "message", "No emergency contacts."));
        }
        else {
            return ResponseEntity.ok(Map.of("success", true, "message", "emergency contacts of employee " + employeeId + " fetched", "emergencyContacts", emergencyContactsOfEmployee));
        }
    }

    @PostMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    public ResponseEntity<Map<String, Object>> addEmergencyContact(@RequestBody CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("employeeId") Long employeeId)throws EmployeeNotFoundException {
        EmergencyContact newEmergencyContact = emergencyContactService.addEmergencyContact(createEmergencyContactDTO, employeeId);
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Emergency contact added", "emergencyContact", newEmergencyContact));
    }

    @PatchMapping("/{emergencyContactId}")
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    public ResponseEntity<Map<String, Object>> updateEmergencyContact(@RequestBody CreateEmergencyContactDTO createEmergencyContactDTO, @PathVariable("emergencyContactId") Long emergencyContactId)throws EntityNotFoundException {
        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(createEmergencyContactDTO, emergencyContactId);
        return ResponseEntity.status(204).body(Map.of("success", true, "message", "Emergency contact updated", "emergencyContact", updatedEmergencyContact));
    }

    @DeleteMapping("/{emergencyContactId}/{employeeId}")
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    public ResponseEntity<Map<String, Object>> deleteEmergencyContact(@PathVariable("emergencyContactId") Long emergencyContactId, @PathVariable("employeeId") Long employeeId)throws EntityNotFoundException {
        emergencyContactService.deleteEmergencyContact(emergencyContactId, employeeId);
        return ResponseEntity.status(204).body(Map.of("success", true, "message","Emergency contact deleted"));
    }

    @ExceptionHandler({
            EmergencyContactService.ValidationException.class,
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmergencyContactService.ValidationException) {
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
