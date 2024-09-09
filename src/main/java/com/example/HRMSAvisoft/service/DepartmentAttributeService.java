package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.AttributeKeyAlreadyExistsException;
import com.example.HRMSAvisoft.repository.DepartmentAttributeRepository;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DepartmentAttributeService {

    private final DepartmentAttributeRepository departmentAttributeRepository;

    private final DepartmentRepository departmentRepository;

    private static final String ATTRIBUTE_KEY_REGEX = "^[a-zA-Z]+( [a-zA-Z]+)*$";

    DepartmentAttributeService(DepartmentAttributeRepository departmentAttributeRepository, DepartmentRepository departmentRepository) {
        this.departmentAttributeRepository = departmentAttributeRepository;
        this.departmentRepository = departmentRepository;
    }
    public List<DepartmentAttribute> getDepartmentAttributes(){
        return departmentAttributeRepository.findAll();
    }

    public DepartmentAttribute addDepartmentAttribute(DepartmentAttribute departmentAttribute)
            throws AttributeKeyAlreadyExistsException, IllegalArgumentException {


        // Check if an attribute with the same key already exists
        DepartmentAttribute existingDepartmentAttribute = departmentAttributeRepository.findByAttributeKey(departmentAttribute.getAttributeKey()).orElse(null);
        if (existingDepartmentAttribute != null) {
            throw new AttributeKeyAlreadyExistsException(departmentAttribute.getAttributeKey() + " department attribute key already exists.");
        }

        // Validate the attribute key
        if (departmentAttribute.getAttributeKey() != null && !departmentAttribute.getAttributeKey().trim().isEmpty()) {
            if (!departmentAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters");
            }
            return departmentAttributeRepository.save(departmentAttribute);
        } else {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }
    }

    public DepartmentAttribute updateDepartmentAttribute(DepartmentAttribute departmentAttribute, Long departmentAttributeId)
            throws AttributeKeyAlreadyExistsException, IllegalArgumentException {

        // Find the department attribute to update
        DepartmentAttribute departmentAttributeToUpdate = departmentAttributeRepository.findById(departmentAttributeId)
                .orElseThrow(() -> new EntityNotFoundException("Department Attribute not found"));

        // Check if an attribute with the same key already exists (but is not the current one)
        DepartmentAttribute existingDepartmentAttribute = departmentAttributeRepository.findByAttributeKey(departmentAttribute.getAttributeKey()).orElse(null);
        if (existingDepartmentAttribute != null && !Objects.equals(existingDepartmentAttribute.getAttributeId(), departmentAttributeId)) {
            throw new AttributeKeyAlreadyExistsException(existingDepartmentAttribute.getAttributeKey() + " department attribute key already exists.");
        }

        // Validate and update the attribute key
        if (Objects.nonNull(departmentAttribute.getAttributeKey()) && !departmentAttribute.getAttributeKey().trim().isEmpty()) {
            if (!departmentAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters");
            }
            departmentAttributeToUpdate.setAttributeKey(departmentAttribute.getAttributeKey());
        } else {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }

        // Save and return the updated department attribute
        return departmentAttributeRepository.save(departmentAttributeToUpdate);
    }

    @Transactional
    public DepartmentAttribute deleteDepartmentAttribute(Long departmentAttributeId) throws EntityNotFoundException {
        DepartmentAttribute departmentAttributeToDelete = departmentAttributeRepository.findById(departmentAttributeId).orElseThrow((() -> new EntityNotFoundException(departmentAttributeId + "not found")));

        List<Department> departmentList = departmentRepository.findAll();
        for(Department department : departmentList){
            department.getAttributes().forEach((k, v)->{
                if(k.equals(departmentAttributeToDelete)){
                    department.getAttributes().remove(departmentAttributeToDelete);
                }
            });
        }

        departmentAttributeRepository.delete(departmentAttributeToDelete);
        return departmentAttributeToDelete;
    }

    public static class DepartmentAlreadyExistsException extends Exception {
        public DepartmentAlreadyExistsException(String departmentName){
            super(departmentName+ " department already exists");
        }
    }

}
