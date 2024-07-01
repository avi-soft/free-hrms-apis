package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.dto.CreateEmployeeDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.User;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmployeeService {


    private  Cloudinary cloudinary;

    private  EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;



    EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary){

        this.employeeRepository = employeeRepository;
        this.cloudinary = cloudinary;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        // Upload file to Cloudinary
        Map<?, ?> params = ObjectUtils.asMap(
                "public_id", "profile_images/" + employeeId, // You can change the public_id format as you need
                "folder", "profile_images" // Optional: folder in Cloudinary to organize your images
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        // Get the URL of the uploaded image
        String imageUrl = (String) uploadResult.get("secure_url");

        // Set the image URL to the employee object and save it
        employee.setProfileImage(imageUrl);
        employeeRepository.save(employee);
    }


    public List<Employee> searchEmployeesByName(String name)throws IllegalArgumentException{
        if(name.equals("") || name == null){
            throw new IllegalArgumentException("Search field empty");
        }
        if(!validateSearchTerm(name)){
            throw new IllegalArgumentException("Only Alphabets allowed");
        }
        List<Employee> searchedEmployees = employeeRepository.searchEmployeesByName(name);
        return searchedEmployees;
    }

    public Employee saveEmployeePersonalInfo(Long employeeId, CreateEmployeeDTO createEmployeeDTO)throws EmployeeNotFoundException, EmployeeCodeAlreadyExistsException{

        if (employeeRepository.existsByEmployeeCode(createEmployeeDTO.getEmployeeCode())) {
            throw new EmployeeCodeAlreadyExistsException("Employee code already exists: " + createEmployeeDTO.getEmployeeCode());
        }
        Employee employeeToAddInfo = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));

        if(createEmployeeDTO.getDepartmentId() != null) {
            Department departmentOfEmployee = departmentRepository.findById(createEmployeeDTO.getDepartmentId()).orElse(null);
            employeeToAddInfo.setDepartment(departmentOfEmployee);

        }

        employeeToAddInfo.setFirstName(createEmployeeDTO.getFirstName());
        employeeToAddInfo.setLastName(createEmployeeDTO.getLastName());
        employeeToAddInfo.setContact(createEmployeeDTO.getContact());
        employeeToAddInfo.setGender(createEmployeeDTO.getGender());
        employeeToAddInfo.setSalary(createEmployeeDTO.getSalary());
        employeeToAddInfo.setEmployeeCode(createEmployeeDTO.getEmployeeCode());
        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
        employeeToAddInfo.setPosition(createEmployeeDTO.getPosition());
        employeeToAddInfo.setJoinDate(createEmployeeDTO.getJoinDate());
        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
        employeeToAddInfo.setDateOfBirth(createEmployeeDTO.getDateOfBirth());

        return employeeRepository.save(employeeToAddInfo);
    }

    public Page<Employee> getAllEmployees(Pageable pageable)throws DataAccessException
    {
       Page<Employee> pageOfEmployees= employeeRepository.findAll(pageable);
        if (pageOfEmployees.isEmpty() && pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageOfEmployees.getTotalPages() - 1, pageable.getPageSize(), pageable.getSort());
            pageOfEmployees = employeeRepository.findAll(pageable);
        }
        return pageOfEmployees;
    }
    public Employee getEmployeeById(Long employeeId)throws EmployeeNotFoundException, NullPointerException
    {
        Employee employee= employeeRepository.getByEmployeeId(employeeId);
        if(employee!=null)return employee;
        else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }



    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    public List<Employee> searchEmployeeByManagerId(Long managerId){
        return employeeRepository.findEmployeesByManagerId(managerId);
    }

    private boolean validateSearchTerm(String term) {
        String pattern = "^[a-zA-Z\\s]+$";
        return Pattern.matches(pattern, term);
    }


    public static class EmployeeCodeAlreadyExistsException extends RuntimeException{
        public EmployeeCodeAlreadyExistsException(String message) {
            super(message);
        }
    }


}
