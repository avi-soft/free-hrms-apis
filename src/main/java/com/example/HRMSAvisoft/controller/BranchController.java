package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/branch")
@Service
public class BranchController {

    private final BranchService branchService;

    BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADD_BRANCH')")
    public ResponseEntity<Map<String,Object>> addBranch(@RequestBody CreateBranchDTO createBranchDTO) throws AttributeKeyDoesNotExistException, BranchService.BranchAlreadyExistsException {
        Branch createdBranch = branchService.addBranch(createBranchDTO);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Branch created successfully", "Branch", createdBranch));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('GET_ALL_BRANCH')")
    public ResponseEntity<Map<String, Object>> getAllBranches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Branch> branchListPage = branchService.getAllBranches(page, size);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branches fetched successfully", "Branches", branchListPage));
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasAuthority('GET_DEPARTMENTS_OF_BRANCH')")
    public ResponseEntity<Map<String, Object>> getDepartmentsOfBranch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("branchId") Long branchId
    ){
        Page<Department> departmentListPage = branchService.getAllDepartmentsOfBranch(page, size, branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Departments fetched successfully", "departmensts", departmentListPage));
    }

    @GetMapping("/unassignedBranches")
    public ResponseEntity<Map<String, Object>> getAllUnassignedBranches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        Page<Branch> unassignedBranches = branchService.getAllUnassignedBranches(page, size);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branches fetched successfully", "Branches", unassignedBranches));
    }


    @PatchMapping("/{branchId}")
    @PreAuthorize("hasAuthority('UPDATE_BRANCH')")
    public ResponseEntity<Map<String, Object>> updateBranch(@RequestBody CreateBranchDTO createBranchDTO, @PathVariable("branchId") Long branchId) throws BranchService.BranchAlreadyExistsException, EntityNotFoundException{
        branchService.updateBranch(createBranchDTO, branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch updated successfully"));
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasAuthority('DELETE_BRANCH')")
    public ResponseEntity<Map<String, Object>> deleteBranch(@PathVariable("branchId") Long branchId)throws EntityNotFoundException{
        branchService.deleteBranch(branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch deleted successfully"));
    }

    @PatchMapping("/{organizationId}/assignBranch/{branchId}")
    @PreAuthorize("hasAuthority('ASSIGN_BRANCH')")
    public ResponseEntity<Map<String, Object>> assignBranchToOrganization(@PathVariable("organizationId") Long organizationId, @PathVariable("branchId") Long branchId)throws EntityNotFoundException{
        branchService.assignBranchToOrganization(organizationId, branchId);

        return ResponseEntity.status(200).body(Map.of("message", "Branch assigned successfully", "success", true));
    }

    @PatchMapping("/{organizationId}/removeBranch/{branchId}")
    @PreAuthorize("hasAuthority('REMOVE_BRANCH')")
    public ResponseEntity<Map<String, Object>> removeBranchFromOrganization(@PathVariable("organizationId") Long organizationId, @PathVariable("branchId") Long branchId)throws EntityNotFoundException{
        branchService.removeBranchFromOrganization(organizationId, branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch removed successfully"));
    }

    @ExceptionHandler({
            AttributeKeyDoesNotExistException.class,
            BranchService.BranchAlreadyExistsException.class,
            EntityNotFoundException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof AttributeKeyDoesNotExistException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if (exception instanceof BranchService.BranchAlreadyExistsException){
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if(exception instanceof EntityNotFoundException){
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
