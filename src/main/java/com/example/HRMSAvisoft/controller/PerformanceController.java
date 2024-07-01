package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AllPerformanceOfEmployeeDTO;
import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.PerformanceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/performance")
@Transactional
public class PerformanceController {

    private final PerformanceService performanceService;

    PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AllPerformanceOfEmployeeDTO>> getAllPerformanceOfEmployee(@PathVariable("employeeId") Long employeeId) throws EmployeeNotFoundException {
        List<Performance> performanceListOfEmployee = performanceService.getAllPerformanceOfEmployee(employeeId);
        List<AllPerformanceOfEmployeeDTO> allPerformanceOfEmployeeDTOs = performanceListOfEmployee.stream().map((performance)->{
            AllPerformanceOfEmployeeDTO allPerformanceOfEmployeeDTO = new AllPerformanceOfEmployeeDTO();
            allPerformanceOfEmployeeDTO.setPerformanceId(performance.getPerformanceId());
            allPerformanceOfEmployeeDTO.setRating(performance.getRating());
            allPerformanceOfEmployeeDTO.setComment(performance.getComment());
            allPerformanceOfEmployeeDTO.setReviewDate(performance.getReviewDate());
            allPerformanceOfEmployeeDTO.setReviewerEmployeeCode(performance.getReviewer().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setReviewerFirstName(performance.getReviewer().getFirstName());
            allPerformanceOfEmployeeDTO.setReviewerLastName(performance.getReviewer().getLastName());
            allPerformanceOfEmployeeDTO.setEmployeeCode(performance.getEmployee().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setFirstName(performance.getEmployee().getFirstName());
            allPerformanceOfEmployeeDTO.setLastName(performance.getEmployee().getLastName());
            return allPerformanceOfEmployeeDTO;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(allPerformanceOfEmployeeDTOs);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addPerformanceOfEmployee(@PathParam("employeeId") Long employeeId, @PathParam("reviewerId") Long reviewerId, @RequestBody CreatePerformanceDTO createPerformanceDTO)throws EmployeeNotFoundException, IllegalAccessException{
        Performance newPerformanceRecord = performanceService.addPerformanceOfEmployee(employeeId, reviewerId, createPerformanceDTO);
        AllPerformanceOfEmployeeDTO allPerformanceOfEmployeeDTO = new AllPerformanceOfEmployeeDTO();
        allPerformanceOfEmployeeDTO.setPerformanceId(newPerformanceRecord.getPerformanceId());
        allPerformanceOfEmployeeDTO.setRating(newPerformanceRecord.getRating());
        allPerformanceOfEmployeeDTO.setComment(newPerformanceRecord.getComment());
        allPerformanceOfEmployeeDTO.setReviewDate(newPerformanceRecord.getReviewDate());
        allPerformanceOfEmployeeDTO.setReviewerEmployeeCode(newPerformanceRecord.getReviewer().getEmployeeCode());
        allPerformanceOfEmployeeDTO.setReviewerFirstName(newPerformanceRecord.getReviewer().getFirstName());
        allPerformanceOfEmployeeDTO.setReviewerLastName(newPerformanceRecord.getReviewer().getLastName());
        allPerformanceOfEmployeeDTO.setEmployeeCode(newPerformanceRecord.getEmployee().getEmployeeCode());
        allPerformanceOfEmployeeDTO.setFirstName(newPerformanceRecord.getEmployee().getFirstName());
        allPerformanceOfEmployeeDTO.setLastName(newPerformanceRecord.getEmployee().getLastName());
        return ResponseEntity.status(201).body(Map.of("success", true, "message", "performance record added", "performance", allPerformanceOfEmployeeDTO));
    }

    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllPerformance(){
        List<Performance> allPerformance = performanceService.getAllPerformance();

        List<AllPerformanceOfEmployeeDTO> allPerformanceOfEmployeeDTOs = allPerformance.stream().map((performance)->{
            AllPerformanceOfEmployeeDTO allPerformanceOfEmployeeDTO = new AllPerformanceOfEmployeeDTO();
            allPerformanceOfEmployeeDTO.setPerformanceId(performance.getPerformanceId());
            allPerformanceOfEmployeeDTO.setRating(performance.getRating());
            allPerformanceOfEmployeeDTO.setComment(performance.getComment());
            allPerformanceOfEmployeeDTO.setReviewDate(performance.getReviewDate());
            allPerformanceOfEmployeeDTO.setReviewerEmployeeCode(performance.getReviewer().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setReviewerFirstName(performance.getReviewer().getFirstName());
            allPerformanceOfEmployeeDTO.setReviewerLastName(performance.getReviewer().getLastName());
            allPerformanceOfEmployeeDTO.setEmployeeCode(performance.getEmployee().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setFirstName(performance.getEmployee().getFirstName());
            allPerformanceOfEmployeeDTO.setLastName(performance.getEmployee().getLastName());
            return allPerformanceOfEmployeeDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.status(200).body(Map.of("success",true,"message","performace fetched successfully", "performanceList",allPerformanceOfEmployeeDTOs));
    }

    @GetMapping("/reviewer/{reviewerId}")
    public ResponseEntity<Map<String, Object>> getPerformanceByReviewer(@PathVariable("reviewerId") Long reviewerId)throws  EntityNotFoundException{
        List<Performance> performanceListOfReviewer = performanceService.getPerformanceByReviewer(reviewerId);
        List<AllPerformanceOfEmployeeDTO> allPerformanceOfEmployeeDTOs = performanceListOfReviewer.stream().map((performance)->{
            AllPerformanceOfEmployeeDTO allPerformanceOfEmployeeDTO = new AllPerformanceOfEmployeeDTO();
            allPerformanceOfEmployeeDTO.setPerformanceId(performance.getPerformanceId());
            allPerformanceOfEmployeeDTO.setRating(performance.getRating());
            allPerformanceOfEmployeeDTO.setComment(performance.getComment());
            allPerformanceOfEmployeeDTO.setReviewDate(performance.getReviewDate());
            allPerformanceOfEmployeeDTO.setReviewerEmployeeCode(performance.getReviewer().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setReviewerFirstName(performance.getReviewer().getFirstName());
            allPerformanceOfEmployeeDTO.setReviewerLastName(performance.getReviewer().getLastName());
            allPerformanceOfEmployeeDTO.setEmployeeCode(performance.getEmployee().getEmployeeCode());
            allPerformanceOfEmployeeDTO.setFirstName(performance.getEmployee().getFirstName());
            allPerformanceOfEmployeeDTO.setLastName(performance.getEmployee().getLastName());
            return allPerformanceOfEmployeeDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.status(200).body(Map.of("success",true, "message","performance fetched successfully", "performanceList", allPerformanceOfEmployeeDTOs));
    }


    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PatchMapping("")
    public ResponseEntity<Map<String, Object>> updatePerformanceOfEmployee(@AuthenticationPrincipal User loggedInUser, @RequestParam("performanceId") Long performanceId, @RequestBody CreatePerformanceDTO createPerformanceDTO)throws EntityNotFoundException, IllegalAccessException {
        Performance updatedPerformance = performanceService.updatePerformanceOfEmployee(loggedInUser, performanceId, createPerformanceDTO);
        return ResponseEntity.status(204).body(Map.of("success",true, "message", "Performance updated successfully.", "performance", updatedPerformance));
    }

    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @DeleteMapping("")
    public ResponseEntity deletePerformanceRecord(@AuthenticationPrincipal User loggedInUser, @RequestParam("performanceId") Long performanceId)throws IllegalAccessException{
        performanceService.deletePerformanceRecord(loggedInUser, performanceId);
        return ResponseEntity.status(204).body(null);
    }


    @ExceptionHandler({
            EmployeeNotFoundException.class,
            EntityNotFoundException.class,
            IllegalAccessException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof IllegalAccessException) {
            message = exception.getMessage();
            status = HttpStatus.FORBIDDEN;
        } else if(exception instanceof EntityNotFoundException) {
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
