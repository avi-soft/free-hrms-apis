package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.EmployeeAttributeRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
public class EmployeeAttributeService {

    private final EmployeeAttributeRepository employeeAttributeRepository;
    private final ModelMapper modelMapper;
    private Cloudinary cloudinary;
    private final EmployeeRepository employeeRepository;
    private static final String ATTRIBUTE_KEY_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";

    EmployeeAttributeService(EmployeeAttributeRepository employeeAttributeRepository, ModelMapper modelMapper, Cloudinary cloudinary, EmployeeRepository employeeRepository) {
        this.employeeAttributeRepository = employeeAttributeRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    public List<EmployeeAttribute> getEmployeeAttributes() {
        List<EmployeeAttribute> employeeAttributes = employeeAttributeRepository.findAll();
        return employeeAttributes;
    }

    public EmployeeAttribute addEmployeeAttribute(EmployeeAttribute employeeAttribute) throws com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException, IllegalArgumentException {
        Pattern pattern = Pattern.compile(ATTRIBUTE_KEY_REGEX);

        // Check if attributeKey matches the pattern
        Matcher matcher = pattern.matcher(employeeAttribute.getAttributeKey().trim());

        // Check if the attributeKey already exists
        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(employeeAttribute.getAttributeKey().trim()).orElse(null);
        if (existingEmployeeAttribute != null) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }

        // Validate that the attributeKey is not empty and matches the regex
        if(employeeAttribute.getAttributeKey() != null && !employeeAttribute.getAttributeKey().equals("") && matcher.matches()) {
            return employeeAttributeRepository.save(employeeAttribute);
        } else {
            throw new IllegalArgumentException("Employee attribute key cannot be empty or contain numbers or special characters.");
        }
    }

    public EmployeeAttribute updateEmployeeAttribute(EmployeeAttribute employeeAttribute, Long employeeAttributeId) throws EntityNotFoundException, IllegalArgumentException {
        EmployeeAttribute employeeAttributeToUpdate = employeeAttributeRepository.findById(employeeAttributeId)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeAttribute not found"));

        // Check if the attribute key already exists for a different ID
        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(employeeAttribute.getAttributeKey()).orElse(null);
        if (existingEmployeeAttribute != null && !Objects.equals(existingEmployeeAttribute.getAttributeId(), employeeAttributeId)) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }

        // Validate and update the attribute key
        if (Objects.nonNull(employeeAttribute.getAttributeKey()) && !employeeAttribute.getAttributeKey().trim().equals("")) {
            if (!employeeAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Employee attribute key is invalid. It should only contain letters and spaces, with no spaces at the start, end, or in between.");
            }
            employeeAttributeToUpdate.setAttributeKey(employeeAttribute.getAttributeKey());
        } else {
            throw new IllegalArgumentException("Employee attribute can't be empty");
        }

        // Save and return the updated entity
        return employeeAttributeRepository.save(employeeAttributeToUpdate);
    }

    @Transactional
    public EmployeeAttribute deleteEmployeeAttribute(Long employeeAttributeId) throws EntityNotFoundException {
        EmployeeAttribute employeeAttributeToDelete = employeeAttributeRepository.findById(employeeAttributeId).orElseThrow((() -> new EntityNotFoundException(employeeAttributeId + "not found")));

        List<Employee> employeeList = employeeRepository.findAll();
        for(Employee employee : employeeList){
            employee.getAttributes().forEach((k, v)->{
                if(k.equals(employeeAttributeToDelete)){
                    employee.getAttributes().remove(employeeAttributeToDelete);
                }
            });
        }

        employeeAttributeRepository.delete(employeeAttributeToDelete);
        return employeeAttributeToDelete;
    }

    public static class EmployeeAttributeAlreadyExistsException extends RuntimeException {
        public EmployeeAttributeAlreadyExistsException(String message) {
            super(message);
        }
    }
}



