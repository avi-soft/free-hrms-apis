package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.entity.LeaveRequest;
import com.example.HRMSAvisoft.entity.LeaveStatus;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.exception.InsufficientLeaveBalanceException;
import com.example.HRMSAvisoft.exception.LeaveRequestNotFoundException;
import com.example.HRMSAvisoft.exception.OverlappingLeaveRequestException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.LeaveBalanceRepository;
import com.example.HRMSAvisoft.repository.LeaveRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional
public class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveService(EmployeeRepository employeeRepository,
                        LeaveRequestRepository leaveRequestRepository,
                        LeaveBalanceRepository leaveBalanceRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public LeaveRequest createLeaveRequest(Long employeeId, LeaveRequest leaveRequest)throws EmployeeNotFoundException ,OverlappingLeaveRequestException,InsufficientLeaveBalanceException{
    Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->new EmployeeNotFoundException(employeeId));
        leaveRequest.setEmployee(employee);
        List<LeaveRequest> overlappingRequests = leaveRequestRepository.findOverlappingLeaveRequests(employeeId, leaveRequest.getStartDate(), leaveRequest.getEndDate());
        if (!overlappingRequests.isEmpty()) {
            throw new OverlappingLeaveRequestException();
        }

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeEmployeeIdAndLeaveTypeLeaveType(
                leaveRequest.getEmployee().getEmployeeId(),
                leaveRequest.getLeaveType()
        ).orElseThrow(() -> new IllegalStateException("Leave balance not found for the employee and leave type"));

        int totalAvailableLeave = leaveBalance.getAccruedLeave() + leaveBalance.getCarryForward();
        if (leaveRequest.getNumberOfDays() > totalAvailableLeave) {
            throw new InsufficientLeaveBalanceException(totalAvailableLeave);
        }

    leaveRequest.setStatus(LeaveStatus.PENDING);
    return leaveRequestRepository.save(leaveRequest);
}
public Page<LeaveRequest> getPendingLeaveRequests(Pageable pageable){
    return leaveRequestRepository.findByStatus(LeaveStatus.PENDING ,pageable);

}
    public LeaveRequest approveLeaveRequest(Long leaveRequestId) throws LeaveRequestNotFoundException, InsufficientLeaveBalanceException {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending leave requests can be approved");
        }

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeEmployeeIdAndLeaveTypeLeaveType(
                leaveRequest.getEmployee().getEmployeeId(),
                leaveRequest.getLeaveType()
        ).orElseThrow(() -> new IllegalStateException("Leave balance not found for the employee and leave type"));

        int totalAvailableLeave = leaveBalance.getAccruedLeave() + leaveBalance.getCarryForward();
        if (leaveRequest.getNumberOfDays() > totalAvailableLeave) {
            throw new InsufficientLeaveBalanceException(totalAvailableLeave);
        }

        leaveBalance.setUsedLeave(leaveBalance.getUsedLeave() + leaveRequest.getNumberOfDays());
        leaveBalance.setAccruedLeave(Math.max(0, leaveBalance.getAccruedLeave() - leaveRequest.getNumberOfDays()));
        leaveBalance.setCarryForward(Math.max(0, totalAvailableLeave - leaveBalance.getUsedLeave()));

        leaveBalanceRepository.save(leaveBalance);

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        return leaveRequestRepository.save(leaveRequest);
    }
    public void declineLeaveRequest(Long id)throws LeaveRequestNotFoundException{
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found"));
        leaveRequest.setStatus(LeaveStatus.DECLINED);
        leaveRequestRepository.save(leaveRequest);
    }


    public Page <LeaveRequest>getPendingLeaveRequestsForEmployee(Long employeeId, Pageable pageable)throws EmployeeNotFoundException{
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return leaveRequestRepository.findByEmployeeAndStatus(employee, LeaveStatus.PENDING, pageable);

    }
    public Page <LeaveRequest>getApprovedLeaveRequestsForEmployee(Long employeeId, Pageable pageable)throws EmployeeNotFoundException{
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return leaveRequestRepository.findByEmployeeAndStatus(employee, LeaveStatus.APPROVED, pageable);

    }
    public Page <LeaveRequest>getDeclinedLeaveRequestsForEmployee(Long employeeId, Pageable pageable)throws EmployeeNotFoundException{
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return leaveRequestRepository.findByEmployeeAndStatus(employee, LeaveStatus.DECLINED, pageable);

    }


    public Page<LeaveRequest>getAllLeaveRequestsForEmployee(Long employeeId,Pageable pageable)throws EmployeeNotFoundException{
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->new EmployeeNotFoundException(employeeId));
        return leaveRequestRepository.findByEmployee(employee,pageable);
    }


//    public int sumOFUnplannedLeavesTakenByEmployeeForAMonth(Long employeeId){
//        return 0;
//    }
}
