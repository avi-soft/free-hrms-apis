package com.example.HRMSAvisoft.scheduler;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.LeaveBalanceRepository;
import com.example.HRMSAvisoft.repository.LeaveTypeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LeaveBalanceScheduler {
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    public LeaveBalanceScheduler(EmployeeRepository employeeRepository,LeaveBalanceRepository leaveBalanceRepository,LeaveTypeRepository leaveTypeRepository){
        this.employeeRepository=employeeRepository;
        this.leaveBalanceRepository=leaveBalanceRepository;
        this.leaveTypeRepository=leaveTypeRepository;
    }


    @Scheduled(cron = "0 0 0 1 * *")
    public void accrueLeavesForAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach(employee -> {
            List<LeaveType> leaveTypes = leaveTypeRepository.findAll();
            leaveTypes.forEach(leaveType -> {
                Optional<LeaveBalance>optionalLeaveBalance = leaveBalanceRepository
                        .findByEmployeeEmployeeIdAndLeaveTypeLeaveType(employee.getEmployeeId(), leaveType.getLeaveType());
                if(optionalLeaveBalance.isPresent()) {
                    LeaveBalance leaveBalance=optionalLeaveBalance.get();
                    if (leaveBalance.getCarryForward() + leaveBalance.getAccruedLeave() <= leaveType.getCarryForwardLimit()) {
                        leaveBalance.setCarryForward(leaveBalance.getCarryForward() + leaveBalance.getAccruedLeave());
                    } else {
                        leaveBalance.setCarryForward(leaveType.getCarryForwardLimit());
                    }
                    leaveBalance.setAccruedLeave(leaveType.getLeavesPerMonth());


                    // Save the updated leave balance
                    leaveBalanceRepository.save(leaveBalance);
                }
            });
        });
    }



}
