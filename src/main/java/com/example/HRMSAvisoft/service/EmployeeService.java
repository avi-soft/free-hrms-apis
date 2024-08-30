package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.dto.CreateEmployeeDTO;
import com.example.HRMSAvisoft.dto.UpdateEmployeeDetailsDTO;
import com.example.HRMSAvisoft.entity.*;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
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
    private DesignationRepository designationRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EmployeeAttributeRepository employeeAttributeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BranchRepository branchRepository;


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

    public Employee saveEmployeePersonalInfo(Long employeeId, CreateEmployeeDTO createEmployeeDTO)throws EmployeeNotFoundException, EmployeeCodeAlreadyExistsException, AccessDeniedException,AttributeKeyDoesNotExistException, EntityNotFoundException{
        createEmployeeDTO.getAttributes().forEach((k,v)->{
            EmployeeAttribute employeeAttribute = employeeAttributeRepository.findByAttributeKey(k).orElse(null);
            if(employeeAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });
        if (employeeRepository.existsByEmployeeCode(createEmployeeDTO.getEmployeeCode())) {
            throw new EmployeeCodeAlreadyExistsException("Employee code already exists: " + createEmployeeDTO.getEmployeeCode());
        }
        Employee employeeToAddInfo = employeeRepository.findById(employeeId).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
        employeeToAddInfo.setGender(createEmployeeDTO.getGender());
        if(createEmployeeDTO.getDepartmentId() != null) {
            Department departmentToAssign = departmentRepository.findById(createEmployeeDTO.getDepartmentId()).orElse(null);
            if(departmentToAssign != null) {
                employeeToAddInfo.getDepartments().add(departmentToAssign);
                departmentToAssign.getEmployees().add(employeeToAddInfo);
            }
        }
        if(createEmployeeDTO.getBranchId() != null) {
            Branch branchToAdd = branchRepository.findById(createEmployeeDTO.getBranchId()).orElse(null);
            if(branchToAdd != null) {
                employeeToAddInfo.setBranch(branchToAdd);
                branchToAdd.getEmployees().add(employeeToAddInfo);
            }
        }

        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        employeeToAddInfo.setCreatedAt(createdAt.format(createdAtFormatter));

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
        for(String designation : createEmployeeDTO.getDesignationList()){
            Designation designationToAdd = designationRepository.findByDesignation(designation).orElseThrow(()->new EntityNotFoundException("Designation "+ designation + " not found"));
            if(!employeeToAddInfo.getDesignations().contains(designationToAdd)) {
                employeeToAddInfo.getDesignations().add(designationToAdd);
            }
        }
        for(String skill : createEmployeeDTO.getSkillList()){
            Skill skillToAdd = skillRepository.findBySkill(skill).orElseThrow(()->new EntityNotFoundException("Skill "+ skill + " not found"));
            if(!employeeToAddInfo.getSkills().contains(skillToAdd)) {
                employeeToAddInfo.getSkills().add(skillToAdd);
            }
        }
        // Create a list of EmployeeAttributeValue
        Map<EmployeeAttribute, String> employeeAttributes = createEmployeeDTO.getAttributes().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> employeeAttributeRepository.findByAttributeKey(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Attribute not found: " + entry.getKey())),
                        Map.Entry::getValue
                ));

        employeeToAddInfo.setAttributes(employeeAttributes);

        return employeeRepository.save(employeeToAddInfo);
    }


    public Page<Employee> getAllEmployees(int page, int size, String sortBy) throws DataAccessException {
        List<Employee> employeesList = employeeRepository.findAll();

        // Sort the list based on the sortBy parameter
        if ("createdAt".equalsIgnoreCase(sortBy)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Sorting by parsing the string dates to LocalDateTime
            employeesList.sort((e1, e2) -> {
                LocalDateTime date1 = LocalDateTime.parse(e1.getCreatedAt(), formatter);
                LocalDateTime date2 = LocalDateTime.parse(e2.getCreatedAt(), formatter);
                return date2.compareTo(date1); // Descending order
            });
        } else if ("name".equalsIgnoreCase(sortBy)) {
            // Sorting by firstName and lastName alphabetically
            employeesList.sort(Comparator.comparing(Employee::getFirstName)
                    .thenComparing(Employee::getLastName));
        }

        Pageable pageable = PageRequest.of(page, size);

        // Paginate the sorted list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), employeesList.size());

        return new PageImpl<>(employeesList.subList(start, end), pageable, employeesList.size());
    }

    public Employee getEmployeeById(Long employeeId)throws EmployeeNotFoundException, NullPointerException,AccessDeniedException
    {
        Employee employee= employeeRepository.getByEmployeeId(employeeId);
        if(employee!=null)
            return employee;
        else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    public Page<Employee> getUnassignedEmployeesOfDepartment(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<Employee> unassignedEmployeesList = employeeRepository.findAllEmployeeWhereDepartmentsIsEmpty();

        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()), unassignedEmployeesList.size());

        return new PageImpl<>(unassignedEmployeesList.subList(start, end), pageable, unassignedEmployeesList.size());
    }

    public Employee updateEmployee(Long employeeId, UpdateEmployeeDetailsDTO updateEmployeeDetailsDTO) throws AccessDeniedException,AttributeKeyDoesNotExistException, EntityNotFoundException {
        updateEmployeeDetailsDTO.getAttributes()
                .forEach((k,v)->{
                    EmployeeAttribute employeeAttribute = employeeAttributeRepository.findByAttributeKey(k).orElse(null);
                    if(employeeAttribute == null){
                        throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
                    }
                });
        Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Employee not found"));

        if(updateEmployeeDetailsDTO.getFirstName()!=null) existingEmployee.setFirstName(updateEmployeeDetailsDTO.getFirstName());
        if(updateEmployeeDetailsDTO.getLastName()!=null) existingEmployee.setLastName(updateEmployeeDetailsDTO.getLastName());

        if(updateEmployeeDetailsDTO.getGender()!=null)existingEmployee.setGender(updateEmployeeDetailsDTO.getGender());
//        if(updateEmployeeDetailsDTO.getDateOfBirth()!=null)existingEmployee.setDateOfBirth(updateEmployeeDetailsDTO.getDateOfBirth());
//        if(updateEmployeeDetailsDTO.getJoinDate()!=null)existingEmployee.setJoinDate(updateEmployeeDetailsDTO.getJoinDate());
//        if(updateEmployeeDetailsDTO.getAdhaarNumber()!=null)existingEmployee.setAdhaarNumber(updateEmployeeDetailsDTO.getAdhaarNumber());
//        if(updateEmployeeDetailsDTO.getUanNumber()!=null)existingEmployee.setUanNumber(updateEmployeeDetailsDTO.getUanNumber());
//        if(updateEmployeeDetailsDTO.getPanNumber()!=null)existingEmployee.setPanNumber(updateEmployeeDetailsDTO.getPanNumber());
//        if(updateEmployeeDetailsDTO.getPosition()!=null)existingEmployee.setPosition(updateEmployeeDetailsDTO.getPosition());
//        if(updateEmployeeDetailsDTO.getSalary()!=0)existingEmployee.setSalary(BigDecimal.valueOf(updateEmployeeDetailsDTO.getSalary()));
        Map<String, String> updatedAttributes = new HashMap<String, String>();
        updateEmployeeDetailsDTO.getAttributes().forEach((k, v)->{
            employeeAttributeRepository.findByAttributeKey(k).orElseThrow(()-> new AttributeKeyDoesNotExistException(k +" does not exist"));
            updatedAttributes.put(k, v);
        });

        if(updateEmployeeDetailsDTO.getDesignationList() != null) {
            existingEmployee.getDesignations().clear();
            for (String designation : updateEmployeeDetailsDTO.getDesignationList()) {
                Designation designationToAdd = designationRepository.findByDesignation(designation).orElseThrow(() -> new EntityNotFoundException("Designation " + designation + " not found"));
                if (!existingEmployee.getDesignations().contains(designationToAdd)) {
                    existingEmployee.getDesignations().add(designationToAdd);
                }
            }
        }

        if(updateEmployeeDetailsDTO.getSkillList() != null) {
            existingEmployee.getSkills().clear();
            for (String skill : updateEmployeeDetailsDTO.getSkillList()) {
                Skill skillToAdd = skillRepository.findBySkill(skill).orElseThrow(() -> new EntityNotFoundException("Skill " + skill + " not found"));
                if (!existingEmployee.getSkills().contains(skillToAdd)) {
                    existingEmployee.getSkills().add(skillToAdd);
                }
            }
        }

        Map<EmployeeAttribute, String> attributeMap = new HashMap<>();

        for (Map.Entry<String, String> entry : updateEmployeeDetailsDTO.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            EmployeeAttribute employeeAttribute= employeeAttributeRepository.findByAttributeKey(key).orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute " + key + " does not exist"));
            attributeMap.put(employeeAttribute, value);
        }
        Map<EmployeeAttribute, String> existingAttributes = existingEmployee.getAttributes();
        existingAttributes.putAll(attributeMap);
        return employeeRepository.save(existingEmployee);
    }

//    public List<Employee> searchEmployeeByManagerId(Long managerId) throws AccessDeniedException{
//        return employeeRepository.findEmployeesByManagerId(managerId);
//    }

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
