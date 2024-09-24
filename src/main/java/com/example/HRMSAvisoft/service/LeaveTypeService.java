package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.LeaveTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository,EmployeeRepository employeeRepository){
        this.leaveTypeRepository=leaveTypeRepository;
        this.employeeRepository=employeeRepository;
    }
    @Transactional
    public LeaveType addLeaveType(LeaveType leaveType) {
        LeaveType createdLeaveType = leaveTypeRepository.save(leaveType);

        return createdLeaveType;
    }

    @Transactional
    public void removeLeaveType(Long leaveTypeId) {

        leaveTypeRepository.deleteById(leaveTypeId);
    }

    public LeaveType updateLeaveType(Long leaveTypeId, LeaveType updatedLeaveType) {
        Optional<LeaveType> existingLeaveTypeOpt = leaveTypeRepository.findById(leaveTypeId);
        if (existingLeaveTypeOpt.isPresent()) {
            LeaveType existingLeaveType = existingLeaveTypeOpt.get();
            if(updatedLeaveType.getLeaveType() != null)
                existingLeaveType.setLeaveType(updatedLeaveType.getLeaveType());
            if(updatedLeaveType.getDescription() != null)
                existingLeaveType.setDescription(updatedLeaveType.getDescription());

            existingLeaveType.setTotalLeaves(updatedLeaveType.getTotalLeaves());

            existingLeaveType.setLeavesPerMonth(updatedLeaveType.getLeavesPerMonth());

            existingLeaveType.setCarryForwardLimit(updatedLeaveType.getCarryForwardLimit());

            return leaveTypeRepository.save(existingLeaveType);
        }
        return null;
    }
    public Page<LeaveType> getAllLeaveTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();

        int start = (int) pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()), leaveTypeList.size() - 1);

        return new PageImpl<>(leaveTypeList.subList(start, end), pageable, leaveTypeList.size());
    }
    public LeaveType getLeaveType(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId).orElse(null);
    }



}
