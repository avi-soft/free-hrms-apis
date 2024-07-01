package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.LeaveBalanceRepository;
import com.example.HRMSAvisoft.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository,EmployeeRepository employeeRepository,LeaveBalanceRepository leaveBalanceRepository){
        this.leaveTypeRepository=leaveTypeRepository;
        this.employeeRepository=employeeRepository;
        this.leaveBalanceRepository=leaveBalanceRepository;

    }
    @Transactional
    public LeaveType addLeaveType(LeaveType leaveType) {
        LeaveType createdLeaveType = leaveTypeRepository.save(leaveType);

          List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveType(createdLeaveType);
            leaveBalance.setAccruedLeave(leaveType.getLeavesPerMonth());
            leaveBalance.setUsedLeave(0);
            leaveBalance.setCarryForward(0);
            leaveBalanceRepository.save(leaveBalance);
        }

        return createdLeaveType;
    }
    @Transactional
    public void removeLeaveType(String leaveType) {
        List<LeaveBalance> leaveBalances = leaveBalanceRepository.findByLeaveTypeLeaveType(leaveType);

          leaveBalanceRepository.deleteAll(leaveBalances);

        leaveTypeRepository.deleteById(leaveType);
    }
    public LeaveType updateLeaveType(String leaveType, LeaveType updatedLeaveType) {
        Optional<LeaveType> existingLeaveTypeOpt = leaveTypeRepository.findById(leaveType);
        if (existingLeaveTypeOpt.isPresent()) {
            LeaveType existingLeaveType = existingLeaveTypeOpt.get();
            existingLeaveType.setDescription(updatedLeaveType.getDescription());
            existingLeaveType.setLeavesPerMonth(updatedLeaveType.getLeavesPerMonth());
            existingLeaveType.setTotalLeaves(updatedLeaveType.getTotalLeaves());
            existingLeaveType.setCarryForwardLimit(updatedLeaveType.getCarryForwardLimit());
            return leaveTypeRepository.save(existingLeaveType);
        }
        return null;
    }
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
    public LeaveType getLeaveType(String leaveType) {
        return leaveTypeRepository.findById(leaveType).orElse(null);
    }



}
