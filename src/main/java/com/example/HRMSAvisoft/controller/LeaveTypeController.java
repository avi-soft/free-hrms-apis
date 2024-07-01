package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.service.LeaveTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/leaveType")
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }
    @PostMapping("addLeaveType")
    public ResponseEntity<Map<String,Object>> addLeaveType(@RequestBody LeaveType leaveType) {
        LeaveType createdLeaveType = leaveTypeService.addLeaveType(leaveType);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Type successfully Added");
        response.put("success", true);
        response.put("leaveType", createdLeaveType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @DeleteMapping("/{leaveType}")
    public ResponseEntity<Map<String,Object>> removeLeaveType(@PathVariable String leaveType) {
        leaveTypeService.removeLeaveType(leaveType);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Type successfully removed");
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/update/{leaveType}")
    public ResponseEntity<Map<String, Object>> updateLeaveType(@PathVariable String leaveType, @RequestBody LeaveType updatedLeaveType) {
        LeaveType updated = leaveTypeService.updateLeaveType(leaveType, updatedLeaveType);
        Map<String, Object> response = new HashMap<>();
        if (updated != null) {
            response.put("message", "Leave Type successfully updated");
            response.put("success", true);
            response.put("leaveType", updated);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Leave Type not found");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLeaveTypes() {
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Types retrieved successfully");
        response.put("success", true);
        response.put("leaveTypes", leaveTypes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{leaveType}")
    public ResponseEntity<Map<String, Object>> getLeaveType(@PathVariable String leaveType) {
        LeaveType leaveTypeObj = leaveTypeService.getLeaveType(leaveType);
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
