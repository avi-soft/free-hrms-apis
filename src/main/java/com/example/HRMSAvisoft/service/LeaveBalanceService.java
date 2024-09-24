package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.LeaveBalanceRepository;
import com.example.HRMSAvisoft.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private  final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository,LeaveTypeRepository leaveTypeRepository,
                               EmployeeRepository employeeRepository){
        this.leaveBalanceRepository=leaveBalanceRepository;
        this.leaveTypeRepository=leaveTypeRepository;
        this.employeeRepository = employeeRepository;
    }
    @Transactional
    public void initializeLeaveBalancesForEmployee(Employee employee) {
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (LeaveType leaveType : leaveTypes) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveType(leaveType);
            leaveBalance.setAccruedLeave(leaveType.getLeavesPerMonth());
            leaveBalance.setUsedLeave(0);
            leaveBalance.setCarryForward(0);
            leaveBalanceRepository.save(leaveBalance);
        }
    }
    public LeaveBalance getLeaveBalanceByEmployeeIdAndLeaveType(Long employeeId, String leaveType) throws Exception{
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->new EmployeeNotFoundException(employeeId));

            return leaveBalanceRepository.findByEmployeeEmployeeIdAndLeaveTypeLeaveType(employeeId,leaveType).get();

    }
}
