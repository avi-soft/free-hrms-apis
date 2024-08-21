package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.exception.AttributeKeyAlreadyExistsException;
import com.example.HRMSAvisoft.service.BranchAttributeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/branchAttribute")
public class BranchAttributeController {

    private final BranchAttributeService branchAttributeService;

    BranchAttributeController(BranchAttributeService branchAttributeService) {
        this.branchAttributeService = branchAttributeService;
    }

//    @PreAuthorize("hasAnyAuthority('ADD_BRANCH_ATTRIBUTE')")
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addBranchAttribute(@RequestBody BranchAttribute branchAttribute) throws AttributeKeyAlreadyExistsException {
        BranchAttribute newBranchAttribute = branchAttributeService.addBranchAttribute(branchAttribute);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Branch created successfully", "attribute", newBranchAttribute));
    }

//    @PreAuthorize("hasAnyAuthority('GET_BRANCH_ATTRIBUTE')")
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getBranchAttributes() {
        List<BranchAttribute> branchAttributeList = branchAttributeService.getBranchAttributes();

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch attributes fetched successfully", "branchAttributes", branchAttributeList));
    }

//    @PreAuthorize("hasAnyAuthority('UPDATE_BRANCH_ATTRIBUTE')")
    @PatchMapping("/{branchAttributeId}")
    public ResponseEntity<Object> updateBranchAttribute(@RequestBody BranchAttribute branchAttribute, @PathVariable("branchAttributeId") Long branchAttributeId) throws AttributeKeyAlreadyExistsException{
        branchAttributeService.updateBranchAttribute(branchAttribute, branchAttributeId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch attribute updated successfully"));
    }

//    @PreAuthorize("hasAnyAuthority('DELETE_BRANCH_ATTRIBUTE')")
    @DeleteMapping("/{branchAttributeId}")
    public ResponseEntity<Object> deleteBranchAttribute(@PathVariable("branchAttributeId") Long branchAttributeId)throws EntityNotFoundException{
        branchAttributeService.deleteBranchAttribute(branchAttributeId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch attribute deleted successfully"));
    }

    @ExceptionHandler({
            AttributeKeyAlreadyExistsException.class,
            EntityNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof AttributeKeyAlreadyExistsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof EntityNotFoundException){
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }else {
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

}
