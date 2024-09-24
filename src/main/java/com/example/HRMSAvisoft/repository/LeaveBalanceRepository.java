package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance,Long> {
    Optional<LeaveBalance> findByEmployeeEmployeeIdAndLeaveTypeLeaveType(Long employeeId, String leaveType);
    List<LeaveBalance> findByLeaveTypeLeaveType(String leaveType);

}
