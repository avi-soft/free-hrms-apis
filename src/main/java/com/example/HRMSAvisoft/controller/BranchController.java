package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.BranchService;
import com.example.HRMSAvisoft.service.DepartmentAttributeService;
import com.example.HRMSAvisoft.service.DepartmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/branch")
@Service
public class BranchController {

    private final BranchService branchService;

    BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PostMapping("")
//    @PreAuthorize("hasAuthority('ADD_BRANCH')")
    public ResponseEntity<Map<String,Object>> addBranch(@RequestBody CreateBranchDTO createBranchDTO) throws AttributeKeyDoesNotExistException, BranchService.BranchAlreadyExistsException {
        Branch createdBranch = branchService.addBranch(createBranchDTO);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "Branch created successfully", "Branch", createdBranch));
    }

    @GetMapping("")
//    @PreAuthorize("hasAuthority('GET_ALL_BRANCH")
    public ResponseEntity<Map<String, Object>> getAllBranches(){
        List<Branch> branchList = branchService.getAllBranches();

//        List<BranchResponseDTO> branchResponseDTOList = branchList.stream().map(branch ->{
//            BranchResponseDTO branchResponseDTO = new BranchResponseDTO();
//            branchResponseDTO.setBranchName(branch.getBranchName());
//            branchResponseDTO.setAttributes(branch.getAttributes());
//            branchResponseDTO.setOrganizationId(branch.getOrganization().getOrganizationId());
//            branchResponseDTO.setOrganizationImage(branch.getOrganization().getOrganizationImage());
//            branchResponseDTO.setOrganizationDescription(branch.getOrganization().getOrganizationDescription());
//
//            return branchResponseDTO;
//        }).collect(Collectors.toList());

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branches fetched successfully", "Branches", branchList));
    }

    @PatchMapping("/{branchId}")
//    @PreAuthorize("hasAuthority('UPDATE_BRANCH')")
    public ResponseEntity<Map<String, Object>> updateBranch(@RequestBody CreateBranchDTO createBranchDTO, @PathVariable("branchId") Long branchId) throws BranchService.BranchAlreadyExistsException, EntityNotFoundException{
        branchService.updateBranch(createBranchDTO, branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch updated successfully"));
    }

    @DeleteMapping("/{branchId}")
//    @PreAuthorize("hasAuthority('DELETE_BRANCH')")
    public ResponseEntity<Map<String, Object>> deleteBranch(@PathVariable("branchId") Long branchId)throws EntityNotFoundException{
        branchService.deleteBranch(branchId);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branch deleted successfully"));
    }

    @PatchMapping("/{organizationId}/assignBranch/{branchId}")
//    @PreAuthorize("hasAuthority('ASSIGN_BRANCH')")
    public ResponseEntity<Map<String, Object>> assignBranchToOrganization(@PathVariable("organizationId") Long organizationId, @PathVariable("branchId") Long branchId)throws EntityNotFoundException{
        branchService.assignBranchToOrganization(organizationId, branchId);

        return ResponseEntity.status(200).body(Map.of("message", "Branch assigned successfully", "success", true));
    }

    @PatchMapping("/{organizationId}/removeBranch/{branchId}")
//    @PreAuthorize("hasAuthority('REMOVE_BRANCH')")
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
