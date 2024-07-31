package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.dto.CreateEmployeeDTO;
import com.example.HRMSAvisoft.entity.*;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeAttributeRepository;
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
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {


    private  Cloudinary cloudinary;

    private  EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeAttributeRepository employeeAttributeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;



    EmployeeService(EmployeeRepository employeeRepository, Cloudinary cloudinary){

        this.employeeRepository = employeeRepository;
        this.cloudinary = cloudinary;
    }

    public void uploadProfileImage(Long employeeId, MultipartFile file)throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException ,AccessDeniedException{
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
        employee.   setProfileImage(imageUrl);
        employeeRepository.save(employee);
    }


    public List<Employee> searchEmployeesByName(String name)throws IllegalArgumentException,AccessDeniedException{
        if(name.equals("") || name == null){
            throw new IllegalArgumentException("Search field empty");
        }
        if(!validateSearchTerm(name)){
            throw new IllegalArgumentException("Only Alphabets allowed");
        }
        List<Employee> searchedEmployees = employeeRepository.searchEmployeesByName(name);
        return searchedEmployees;
    }

    public Employee saveEmployeePersonalInfo(Long employeeId, CreateEmployeeDTO createEmployeeDTO,Map<String, String> attributes)throws EmployeeNotFoundException, EmployeeCodeAlreadyExistsException, AccessDeniedException {

        if (employeeRepository.existsByEmployeeCode(createEmployeeDTO.getEmployeeCode())) {
            throw new EmployeeCodeAlreadyExistsException("Employee code already exists: " + createEmployeeDTO.getEmployeeCode());
        }
        Employee employeeToAddInfo = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));

        if(createEmployeeDTO.getDepartmentId() != null) {
            Department departmentOfEmployee = departmentRepository.findById(createEmployeeDTO.getDepartmentId()).orElse(null);
            employeeToAddInfo.setDepartment(departmentOfEmployee);
        }

//        employeeToAddInfo.setFirstName(createEmployeeDTO.getFirstName());
//        employeeToAddInfo.setLastName(createEmployeeDTO.getLastName());
//        employeeToAddInfo.setContact(createEmployeeDTO.getContact());
//        employeeToAddInfo.setGender(createEmployeeDTO.getGender());
//        employeeToAddInfo.setSalary(createEmployeeDTO.getSalary());
//        employeeToAddInfo.setEmployeeCode(createEmployeeDTO.getEmployeeCode());
//        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
//        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
//        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
//        employeeToAddInfo.setPosition(createEmployeeDTO.getPosition());
//        employeeToAddInfo.setJoinDate(createEmployeeDTO.getJoinDate());
//        employeeToAddInfo.setAdhaarNumber(createEmployeeDTO.getAdhaarNumber());
//        employeeToAddInfo.setPanNumber(createEmployeeDTO.getPanNumber());
//        employeeToAddInfo.setUanNumber(createEmployeeDTO.getUanNumber());
//        employeeToAddInfo.setDateOfBirth(createEmployeeDTO.getDateOfBirth());
        employeeToAddInfo.setEmployeeCode(createEmployeeDTO.getEmployeeCode());
        employeeToAddInfo.setFirstName(createEmployeeDTO.getFirstName());
        employeeToAddInfo.setLastName(createEmployeeDTO.getLastName());
        // Create a list of EmployeeAttributeValue
        Map<EmployeeAttribute, String> employeeAttributes = attributes.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> employeeAttributeRepository.findByAttributeKey(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Attribute not found: " + entry.getKey())),
                        Map.Entry::getValue
                ));

        employeeToAddInfo.setAttributes(employeeAttributes);
        return employeeRepository.save(employeeToAddInfo);
    }

    public Page<Employee> getAllEmployees(Pageable pageable)throws DataAccessException,AccessDeniedException
    {
       Page<Employee> pageOfEmployees= employeeRepository.findAll(pageable);
        if (pageOfEmployees.isEmpty() && pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageOfEmployees.getTotalPages() - 1, pageable.getPageSize(), pageable.getSort());
            pageOfEmployees = employeeRepository.findAll(pageable);
        }
        return pageOfEmployees;
    }
    public Employee getEmployeeById(Long employeeId)throws EmployeeNotFoundException, NullPointerException,AccessDeniedException
    {
        Employee employee= employeeRepository.getByEmployeeId(employeeId);
        if(employee!=null)return employee;
        else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    public Employee updateEmployee(Employee updatedEmployee) throws AccessDeniedException {
        return employeeRepository.save(updatedEmployee);
    }

    public List<Employee> searchEmployeeByManagerId(Long managerId) throws AccessDeniedException{
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
