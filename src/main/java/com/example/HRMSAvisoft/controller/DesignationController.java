package com.example.HRMSAvisoft.controller;


import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Designation;
import com.example.HRMSAvisoft.service.DesignationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/designation")
@Transactional
public class DesignationController {

    private final DesignationService designationService;

    DesignationController(DesignationService designationService){
        this.designationService = designationService;
    }

    @PreAuthorize("hasAnyAuthority('ADD_DESIGNATION')")
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addDesignation(@RequestBody Designation designation)throws IllegalArgumentException{
        Designation newDesignation = designationService.addDesignation(designation);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Designation added successfully", "designation", newDesignation));
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_DESIGNATION')")
    @GetMapping("")
    public ResponseEntity<Page<Designation>> getAllDesignations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Designation> designationList = designationService.getAllDesignations(page, size);

        return ResponseEntity.ok(designationList);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_DESIGNATION')")
    @PatchMapping("/{designationId}")
    public ResponseEntity<Map<String, Object>> updateDesignation(@RequestBody Designation designation, @PathVariable("designationId") Long designationId) throws EntityNotFoundException, IllegalArgumentException{
        designationService.updateDesignation(designation, designationId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Designation updated successfully"));
    }

    @PreAuthorize("hasAnyAuthority('DELETE_DESIGNATION')")
    @DeleteMapping("/{designationId}")
    public ResponseEntity<Map<String, Object>> deleteDesignation(@PathVariable("designationId") Long designationId)throws EntityNotFoundException{
        designationService.deleteDesignation(designationId);

        return ResponseEntity.status(200).body(Map.of("success",true, "message", "Designation deleted successfully"));
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof IllegalArgumentException) {
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
