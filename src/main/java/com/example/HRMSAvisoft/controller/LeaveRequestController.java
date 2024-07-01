package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.LeaveRequestDTO;
import com.example.HRMSAvisoft.entity.LeaveRequest;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.exception.LeaveRequestNotFoundException;
import com.example.HRMSAvisoft.service.LeaveService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/leave")
public class LeaveRequestController
{
    private LeaveService leaveService;
    private ModelMapper modelMapper;
    public LeaveRequestController(LeaveService leaveService,ModelMapper modelMapper){
        this.leaveService=leaveService;
        this.modelMapper=modelMapper;
    }
    @PreAuthorize("hasAuthority('Role_Employee')")
    @PostMapping("/{employeeId}/leaveRequest")
    public ResponseEntity<Map<String,Object>>createLeaveRequest(@PathVariable Long employeeId, @RequestBody LeaveRequestDTO leaveRequestDTO)throws Exception{
        Map<String,Object>response=new HashMap<>();
        LeaveRequest leaveRequest=modelMapper.map(leaveRequestDTO,LeaveRequest.class);
        LeaveRequest sentRequest=leaveService.createLeaveRequest(employeeId,leaveRequest);
        if(sentRequest!=null) {
            response.put("Message", "Leave Request successfully sent");
            response.put("Success", true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("Message","Unexpected Error occurred");
        response.put("Success",false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @GetMapping("/getLeaveRequests")
    public ResponseEntity<Map<String,Object>>getLeaveRequests(@RequestParam (defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size){
        Map<String, Object>response =new HashMap<>();
        if(page<0||size<=0){
            response.put("Message","Invalid page or size parameters");
            response.put("Success",false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> leaveRequests = leaveService.getPendingLeaveRequests(pageable);

        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequests.getContent().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestDTO.class))
                .collect(Collectors.toList());
        if (leaveRequests.isEmpty()) {
            response.put("Message", "No pending leave requests found");
            response.put("Success", true);
            response.put("leaveRequests", leaveRequestDTOs);
            response.put("currentPage", leaveRequests.getNumber());
            response.put("totalItems", leaveRequests.getTotalElements());
            response.put("totalPages", leaveRequests.getTotalPages());
            return ResponseEntity.ok(response);
        }

        response.put("leaveRequests", leaveRequestDTOs);
        response.put("currentPage", leaveRequests.getNumber());
        response.put("totalItems", leaveRequests.getTotalElements());
        response.put("totalPages", leaveRequests.getTotalPages());
        response.put("Success", true);

        return ResponseEntity.ok(response);


    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PatchMapping("/declineLeaveRequest/{id}")
    public ResponseEntity<Map<String,Object>>DeclineLeaveRequest(@PathVariable Long id)throws LeaveRequestNotFoundException {
        leaveService.declineLeaveRequest(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Leave Type successfully removed");
        response.put("success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PatchMapping("/{leaveRequestId}/approve")
    public ResponseEntity<Map<String, Object>> approveLeaveRequest(@PathVariable Long leaveRequestId) {
        Map<String, Object> response = new HashMap<>();
        try {
            LeaveRequest approvedLeaveRequest = leaveService.approveLeaveRequest(leaveRequestId);
            LeaveRequestDTO approvedLeaveRequestDTO=modelMapper.map(approvedLeaveRequest, LeaveRequestDTO.class);
            response.put("message", "Leave request approved successfully");
            response.put("success", true);
            response.put("leaveRequest", approvedLeaveRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @PreAuthorize("hasAnyAuthority('Role_Employee','Role_Superadmin','Role_Admin')")
    @GetMapping("getLeaveRequestsForEmployee/{employeeId}")
    public ResponseEntity<Map<String,Object>>getLeaveRequestForEmployee(@PathVariable Long employeeId
            ,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int  size) throws  Exception{
        Map<String, Object> response = new HashMap<>();

        if (page < 0 || size <= 0) {
            response.put("message", "Invalid page or size parameters");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> leaveRequestsPage = leaveService.getAllLeaveRequestsForEmployee(employeeId, pageable);
        if (leaveRequestsPage.isEmpty()) {
            response.put("message", "No leave requests found");
            response.put("success", true);
            response.put("leaveRequests", leaveRequestsPage.getContent());
            response.put("currentPage", leaveRequestsPage.getNumber());
            response.put("totalItems", leaveRequestsPage.getTotalElements());
            response.put("totalPages", leaveRequestsPage.getTotalPages());
            return ResponseEntity.ok(response);
        }
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestsPage.getContent().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestDTO.class))
                .toList();

        response.put("leaveRequests",leaveRequestDTOs);
        response.put("currentPage", leaveRequestsPage.getNumber());
        response.put("totalItems", leaveRequestsPage.getTotalElements());
        response.put("totalPages", leaveRequestsPage.getTotalPages());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('Role_Employee','Role_Superadmin','Role_Admin')")
    @GetMapping("/pendingLeaveRequestsForEmployee")
    public ResponseEntity<Map<String, Object>> getPendingLeaveRequestsForEmployee(
            @RequestParam Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws EmployeeNotFoundException {

        Map<String, Object> response = new HashMap<>();

        if (page < 0 || size <= 0) {
            response.put("message", "Invalid page or size parameters");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> leaveRequestsPage = leaveService.getPendingLeaveRequestsForEmployee(employeeId, pageable);

        if (leaveRequestsPage.isEmpty()) {
            response.put("message", "No Pending leave requests found");
            response.put("success", true);
            response.put("pendingLeaveRequests", leaveRequestsPage.getContent());
            response.put("currentPage", leaveRequestsPage.getNumber());
            response.put("totalItems", leaveRequestsPage.getTotalElements());
            response.put("totalPages", leaveRequestsPage.getTotalPages());
            return ResponseEntity.ok(response);
        }
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestsPage.getContent().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestDTO.class))
                .toList();

        response.put("pendingLeaveRequests",leaveRequestDTOs);
        response.put("currentPage", leaveRequestsPage.getNumber());
        response.put("totalItems", leaveRequestsPage.getTotalElements());
        response.put("totalPages", leaveRequestsPage.getTotalPages());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('Role_Employee','Role_Superadmin','Role_Admin')")
    @GetMapping("/approvedLeaveRequestsForEmployee")
    public ResponseEntity<Map<String, Object>> getApprovedLeaveRequestsForEmployee(
            @RequestParam Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws EmployeeNotFoundException {

        Map<String, Object> response = new HashMap<>();

        if (page < 0 || size <= 0) {
            response.put("message", "Invalid page or size parameters");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> leaveRequestsPage = leaveService.getApprovedLeaveRequestsForEmployee(employeeId, pageable);

        if (leaveRequestsPage.isEmpty()) {
            response.put("message", "No Approved leave requests found");
            response.put("success", true);
            response.put("approvedLeaveRequests", leaveRequestsPage.getContent());
            response.put("currentPage", leaveRequestsPage.getNumber());
            response.put("totalItems", leaveRequestsPage.getTotalElements());
            response.put("totalPages", leaveRequestsPage.getTotalPages());
            return ResponseEntity.ok(response);
        }
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestsPage.getContent().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestDTO.class))
                .toList();

        response.put("approvedLeaveRequests",leaveRequestDTOs);
        response.put("currentPage", leaveRequestsPage.getNumber());
        response.put("totalItems", leaveRequestsPage.getTotalElements());
        response.put("totalPages", leaveRequestsPage.getTotalPages());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAnyAuthority('Role_Employee','Role_Superadmin','Role_Admin')")
    @GetMapping("/declinedLeaveRequestsForEmployee")
    public ResponseEntity<Map<String, Object>> getDeclinedLeaveRequestsForEmployee(
            @RequestParam Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) throws EmployeeNotFoundException {

        Map<String, Object> response = new HashMap<>();

        if (page < 0 || size <= 0) {
            response.put("message", "Invalid page or size parameters");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<LeaveRequest> leaveRequestsPage = leaveService.getDeclinedLeaveRequestsForEmployee(employeeId, pageable);

        if (leaveRequestsPage.isEmpty()) {
            response.put("message", "No Declined leave requests found");
            response.put("success", true);
            response.put("declinedLeaveRequests", leaveRequestsPage.getContent());
            response.put("currentPage", leaveRequestsPage.getNumber());
            response.put("totalItems", leaveRequestsPage.getTotalElements());
            response.put("totalPages", leaveRequestsPage.getTotalPages());
            return ResponseEntity.ok(response);
        }
        List<LeaveRequestDTO> leaveRequestDTOs = leaveRequestsPage.getContent().stream()
                .map(leaveRequest -> modelMapper.map(leaveRequest, LeaveRequestDTO.class))
                .toList();

        response.put("declinedLeaveRequests",leaveRequestDTOs);
        response.put("currentPage", leaveRequestsPage.getNumber());
        response.put("totalItems", leaveRequestsPage.getTotalElements());
        response.put("totalPages", leaveRequestsPage.getTotalPages());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}
