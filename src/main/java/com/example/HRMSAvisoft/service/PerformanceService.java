package com.example.HRMSAvisoft.service;


import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final EmployeeRepository employeeRepository;

    PerformanceService(PerformanceRepository performanceRepository, EmployeeRepository employeeRepository) {
        this.performanceRepository = performanceRepository;
        this.employeeRepository = employeeRepository;
    }
    public List<Performance> getAllPerformanceOfEmployee(Long employeeId)throws  EmployeeNotFoundException{
        Employee employeeToFindPerformance = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
        return employeeToFindPerformance.getPerformanceList();
    }

    public Performance addPerformanceOfEmployee(Long employeeId, Long reviewerId, CreatePerformanceDTO createPerformanceDTO)throws EmployeeNotFoundException, IllegalAccessException{
        Employee employeeToAddPerformance = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
        if(employeeToAddPerformance.getDepartment().getManager().getEmployeeId() != reviewerId){
            throw new IllegalAccessException("Forbidden to give Performance Report.");
        }
        Employee reviewer = employeeRepository.findById(reviewerId).orElse(null);
        Performance newPerformanceRecord = new Performance();
        newPerformanceRecord.setComment(createPerformanceDTO.getComment());
        newPerformanceRecord.setRating(createPerformanceDTO.getRating());
        newPerformanceRecord.setEmployee(employeeToAddPerformance);
        newPerformanceRecord.setReviewer(reviewer);
        LocalDateTime reviewedAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newPerformanceRecord.setReviewDate(createdAtFormatter.format(reviewedAt));
        performanceRepository.save(newPerformanceRecord);
        employeeToAddPerformance.getPerformanceList().add(newPerformanceRecord);
        employeeRepository.save(employeeToAddPerformance);

        return newPerformanceRecord;
    }

    public Performance updatePerformanceOfEmployee(User loggedInUser, Long performanceId, CreatePerformanceDTO createPerformanceDTO)throws IllegalAccessException, EntityNotFoundException{
        Performance performanceToUpdate = performanceRepository.findById(performanceId).orElseThrow(()-> new EntityNotFoundException("Performance record not found."));

        if(loggedInUser.getEmployee().getEmployeeId() != performanceToUpdate.getReviewer().getEmployeeId()){
            throw new IllegalAccessException(("Forbidden to update performance."));
        }
        if(createPerformanceDTO.getComment() != null || createPerformanceDTO.getComment() != ""){
            performanceToUpdate.setComment(createPerformanceDTO.getComment());
        }
        if(createPerformanceDTO.getRating() != null){
            performanceToUpdate.setRating(createPerformanceDTO.getRating());
        }
        return performanceRepository.save(performanceToUpdate);
    }

    public List<Performance> getAllPerformance(){
        return performanceRepository.findAll();
    }

    public List<Performance> getPerformanceByReviewer(Long reviewerId)throws EntityNotFoundException{
        Employee reviewer = employeeRepository.findById(reviewerId).orElseThrow(()-> new EntityNotFoundException("Reviewer not found"));
        return performanceRepository.findByReviewer(reviewer);
    }

    public void deletePerformanceRecord(User loggedInUser, Long performanceId)throws IllegalAccessException{
        Performance performanceToDelete = performanceRepository.findById(performanceId).orElseThrow(()-> new EntityNotFoundException("Performance record not found"));
        if(loggedInUser.getEmployee().getEmployeeId() != performanceToDelete.getReviewer().getEmployeeId()){
            throw new IllegalAccessException(("Forbidden to delete performance."));
        }
        Employee employeeWithPerformanceToDelete = employeeRepository.findById(performanceToDelete.getEmployee().getEmployeeId()).orElseThrow(()-> new EntityNotFoundException("Employee not found."));
        employeeWithPerformanceToDelete.getPerformanceList().remove(performanceToDelete);
    }
}
