package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.entity.*;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.PerformanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTests {

    @InjectMocks
    PerformanceService performanceService;

    @Mock
    PerformanceRepository performanceRepository;

    @Mock
    EmployeeRepository employeeRepository;
    @Test
    @DisplayName("test_returnsPerformanceListForValidId")
    public void test_returns_performance_list_for_valid_id() throws EmployeeNotFoundException {
        
        EmployeeRepository mockEmployeeRepository = mock(EmployeeRepository.class);
        PerformanceRepository mockPerformanceRepository = mock(PerformanceRepository.class);
        PerformanceService performanceService = new PerformanceService(mockPerformanceRepository, mockEmployeeRepository);
        Long validEmployeeId = 1L;
        Employee employee = new Employee();
        employee.setPerformanceList(Arrays.asList(new Performance(), new Performance()));
        when(mockEmployeeRepository.findById(validEmployeeId)).thenReturn(Optional.of(employee));


        List<Performance> performances = performanceService.getAllPerformanceOfEmployee(validEmployeeId);

        assertNotNull(performances);
        assertEquals(2, performances.size());
    }

    @Test
    @DisplayName("test_addPerformanceSuccess")
    public void test_add_performance_success() throws EmployeeNotFoundException, IllegalAccessException {
        PerformanceRepository performanceRepository = mock(PerformanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        PerformanceService performanceService = new PerformanceService(performanceRepository, employeeRepository);

        Employee manager = new Employee();
        manager.setEmployeeId(1L);

        Department department = new Department();
        department.setManager(manager);

        Employee employee = new Employee();
        employee.setEmployeeId(2L);
        employee.setDepartment(department);

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));

        CreatePerformanceDTO createPerformanceDTO = new CreatePerformanceDTO();
        createPerformanceDTO.setComment("Great job!");
        createPerformanceDTO.setRating(Rating.EXCELLENT);

        Performance performance = performanceService.addPerformanceOfEmployee(2L, 1L, createPerformanceDTO);

        assertNotNull(performance);
        assertEquals("Great job!", performance.getComment());
        assertEquals(Rating.EXCELLENT, performance.getRating());
    }

    @Test
    @DisplayName("test_UpdatePerformanceSuccess")
    public void test_update_performance_comment_and_rating_Sucess() throws IllegalAccessException {
        PerformanceRepository performanceRepository = mock(PerformanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        PerformanceService performanceService = new PerformanceService(performanceRepository, employeeRepository);

        User loggedInUser = new User();
        Employee reviewer = new Employee();
        reviewer.setEmployeeId(1L);
        loggedInUser.setEmployee(reviewer);

        Performance performance = new Performance();
        performance.setPerformanceId(1L);
        performance.setReviewer(reviewer);

        CreatePerformanceDTO createPerformanceDTO = new CreatePerformanceDTO();
        createPerformanceDTO.setComment("Updated comment");
        createPerformanceDTO.setRating(Rating.EXCELLENT);

        when(performanceRepository.findById(1L)).thenReturn(Optional.of(performance));
        when(performanceRepository.save(performance)).thenReturn(performance);

        Performance updatedPerformance = performanceService.updatePerformanceOfEmployee(loggedInUser, 1L, createPerformanceDTO);

        assertEquals("Updated comment", updatedPerformance.getComment());
        assertEquals(Rating.EXCELLENT, updatedPerformance.getRating());
    }

    @Test
    @DisplayName("test_returnListOfAllPerformanceRecords")
    public void test_return_all_performance_records_when_records_exist() {
        PerformanceRepository performanceRepository = mock(PerformanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        PerformanceService performanceService = new PerformanceService(performanceRepository, employeeRepository);

        List<Performance> performanceList = List.of(new Performance(), new Performance());
        when(performanceRepository.findAll()).thenReturn(performanceList);

        List<Performance> result = performanceService.getAllPerformance();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("test_reviewerAddedPerformanceRecordsList")
    public void test_reviewer_exists_and_has_performance_records() {
        PerformanceRepository performanceRepository = mock(PerformanceRepository.class);
        EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
        PerformanceService performanceService = new PerformanceService(performanceRepository, employeeRepository);

        Employee reviewer = new Employee();
        reviewer.setEmployeeId(1L);

        Performance performance1 = new Performance();
        performance1.setReviewer(reviewer);
        Performance performance2 = new Performance();
        performance2.setReviewer(reviewer);

        List<Performance> performanceList = List.of(performance1, performance2);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(reviewer));
        when(performanceRepository.findByReviewer(reviewer)).thenReturn(performanceList);

        List<Performance> result = performanceService.getPerformanceByReviewer(1L);

        assertEquals(2, result.size());
        assertEquals(reviewer, result.get(0).getReviewer());
        assertEquals(reviewer, result.get(1).getReviewer());
    }

}
