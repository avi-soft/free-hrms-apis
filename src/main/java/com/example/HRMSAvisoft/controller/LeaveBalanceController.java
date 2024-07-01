package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.LeaveBalanceDTO;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.service.LeaveBalanceService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/leaveBalance")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;
    private final ModelMapper modelMapper;
    public LeaveBalanceController(LeaveBalanceService leaveBalanceService,ModelMapper modelMapper){
        this.leaveBalanceService=leaveBalanceService;
        this.modelMapper=modelMapper;
    }
    @GetMapping("/{employeeId}")
    public ResponseEntity<Map<String,Object>> getLeaveBalanceForEmployee(@PathVariable long employeeId, @RequestBody String leaveType)throws Exception{

       LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalanceByEmployeeIdAndLeaveType(employeeId, leaveType);
       LeaveBalanceDTO leaveBalanceDTO=modelMapper.map(leaveBalance,LeaveBalanceDTO.class);
        return ResponseEntity.ok(Map.of("success", true, "message", "Employee created Successfully", "LeaveBalance",leaveBalanceDTO ));


    }
}
