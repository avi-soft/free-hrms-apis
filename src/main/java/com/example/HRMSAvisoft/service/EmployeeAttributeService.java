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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        String attributeKey = employeeAttribute.getAttributeKey().trim();
        Matcher matcher = pattern.matcher(attributeKey);

        // Check if the attributeKey already exists
        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(attributeKey).orElse(null);
        if (existingEmployeeAttribute != null) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }

        // Validate that the attributeKey is not empty
        if (attributeKey == null || attributeKey.equals("")) {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }

        // Validate that the attributeKey matches the regex (no numbers or special characters)
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters.");
        }

        // If all validations pass, save the EmployeeAttribute
        return employeeAttributeRepository.save(employeeAttribute);
    }

    public EmployeeAttribute updateEmployeeAttribute(EmployeeAttribute employeeAttribute, Long employeeAttributeId) throws EntityNotFoundException, IllegalArgumentException {
        // Find the employee attribute to update
        EmployeeAttribute employeeAttributeToUpdate = employeeAttributeRepository.findById(employeeAttributeId)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeAttribute not found"));

        // Check if the attribute key already exists for a different ID
        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(employeeAttribute.getAttributeKey()).orElse(null);
        if (existingEmployeeAttribute != null && !Objects.equals(existingEmployeeAttribute.getAttributeId(), employeeAttributeId)) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }

        // Validate and update the attribute key
        String attributeKey = employeeAttribute.getAttributeKey();
        if (attributeKey == null || attributeKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }

        // Check for invalid attribute key
        if (!attributeKey.trim().matches(ATTRIBUTE_KEY_REGEX)) {
            throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters");
        }

        // Set the attribute key if valid
        employeeAttributeToUpdate.setAttributeKey(attributeKey.trim());

        // Save and return the updated entity
        return employeeAttributeRepository.save(employeeAttributeToUpdate);
    }

    @Transactional
    public EmployeeAttribute deleteEmployeeAttribute(Long employeeAttributeId) throws EntityNotFoundException {
        // Find the EmployeeAttribute to delete, or throw an exception if not found
        EmployeeAttribute employeeAttributeToDelete = employeeAttributeRepository.findById(employeeAttributeId)
                .orElseThrow(() -> new EntityNotFoundException(employeeAttributeId + " not found"));

        // Find all employees and update their attribute map
        List<Employee> employeeList = employeeRepository.findAll();
        for (Employee employee : employeeList) {
            // Use an iterator to avoid ConcurrentModificationException
            Iterator<Map.Entry<EmployeeAttribute, String>> iterator = employee.getAttributes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<EmployeeAttribute, String> entry = iterator.next();
                if (entry.getKey().equals(employeeAttributeToDelete)) {
                    iterator.remove();  // Safely remove the entry using the iterator
                }
            }
            // Persist the changes by saving the employee entity
            employeeRepository.save(employee);
        }

        // After removing the attribute from all employees, delete the attribute from the database
        employeeAttributeRepository.delete(employeeAttributeToDelete);
        return employeeAttributeToDelete;
    }
    public static class EmployeeAttributeAlreadyExistsException extends RuntimeException {
        public EmployeeAttributeAlreadyExistsException(String message) {
            super(message);
        }
    }
}



