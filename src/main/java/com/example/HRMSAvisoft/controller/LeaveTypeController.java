package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.service.LeaveTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/leaveType")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    @PreAuthorize("hasAuthority('ADD_LEAVE_TYPE')")
    @PostMapping("/addLeaveType")
    public ResponseEntity<Map<String,Object>> addLeaveType(@RequestBody LeaveType leaveType) {
        LeaveType createdLeaveType = leaveTypeService.addLeaveType(leaveType);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Type successfully Added");
        response.put("success", true);
        response.put("leaveType", createdLeaveType);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('REMOVE_LEAVE_TYPE')")
    @DeleteMapping("/{leaveTypeId}")
    public ResponseEntity<Map<String,Object>> removeLeaveType(@PathVariable Long leaveTypeId) {
        leaveTypeService.removeLeaveType(leaveTypeId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Type successfully removed");
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('UPDATE_LEAVE_TYPE')")
    @PatchMapping("/{leaveTypeId}")
    public ResponseEntity<Map<String, Object>> updateLeaveType(@PathVariable Long leaveTypeId, @RequestBody LeaveType updateLeaveTypeToNew) {
        LeaveType updatedLeaveType = leaveTypeService.updateLeaveType(leaveTypeId, updateLeaveTypeToNew);
        Map<String, Object> response = new HashMap<>();
        if (updatedLeaveType != null) {
            response.put("message", "Leave Type successfully updated");
            response.put("success", true);
            response.put("leaveType", updatedLeaveType);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Leave Type not found");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PreAuthorize("hasAuthority('GET_ALL_LEAVE_TYPES')")
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllLeaveTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LeaveType> leaveTypePage = leaveTypeService.getAllLeaveTypes(page,size);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Types retrieved successfully");
        response.put("success", true);
        response.put("leaveTypes", leaveTypePage);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasAuthority('GET_LEAVE_TYPE')")
    @GetMapping("/{leaveTypeId}")
    public ResponseEntity<Map<String, Object>> getLeaveType(@PathVariable Long leaveTypeId) {
        LeaveType leaveTypeObj = leaveTypeService.getLeaveType(leaveTypeId);
        Map<String, Object> response = new HashMap<>();
        if (leaveTypeObj != null) {
            response.put("message", "Leave Type retrieved successfully");
            response.put("success", true);
            response.put("leaveType", leaveTypeObj);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Leave Type not found");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}
