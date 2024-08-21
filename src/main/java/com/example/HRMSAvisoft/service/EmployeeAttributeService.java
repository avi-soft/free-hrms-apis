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


@Service
@Transactional
public class EmployeeAttributeService {

    private final EmployeeAttributeRepository employeeAttributeRepository;
    private final ModelMapper modelMapper;
    private Cloudinary cloudinary;
    private final EmployeeRepository employeeRepository;

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
        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(employeeAttribute.getAttributeKey()).orElse(null);
        if (existingEmployeeAttribute != null) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }
        return employeeAttributeRepository.save(employeeAttribute);
    }

    public EmployeeAttribute updateEmployeeAttribute(EmployeeAttribute employeeAttribute, Long employeeAttributeId) throws EntityNotFoundException, IllegalArgumentException {
        EmployeeAttribute employeeAttributeToUpdate = employeeAttributeRepository.findById(employeeAttributeId).orElseThrow((() -> new EntityNotFoundException("EmployeeAttribute not found")));

        EmployeeAttribute existingEmployeeAttribute = employeeAttributeRepository.findByAttributeKey(employeeAttribute.getAttributeKey()).orElse(null);
        if (existingEmployeeAttribute != null && !Objects.equals(existingEmployeeAttribute.getAttributeId(), employeeAttributeId)) {
            throw new com.example.HRMSAvisoft.service.EmployeeAttributeService.EmployeeAttributeAlreadyExistsException(existingEmployeeAttribute.getAttributeKey() + " employeeAttribute already exists");
        }

        if (Objects.nonNull(employeeAttribute.getAttributeKey())) {
            employeeAttributeToUpdate.setAttributeKey(employeeAttribute.getAttributeKey());
        }
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



