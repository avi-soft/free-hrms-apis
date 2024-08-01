package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.*;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentAttributeRepository;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    private final OrganizationRepository organizationRepository;

    private final DepartmentAttributeRepository departmentAttributeRepository;

    DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository, DepartmentAttributeRepository departmentAttributeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
        this.departmentAttributeRepository = departmentAttributeRepository;
    }

//    public List<Department> getAllDepartments(Long organizationId) {
//        return departmentRepository.findAllByOrganizationId(organizationId);
//    }

    public Department addDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, Map<String, String> attributes) throws EmployeeNotFoundException, EntityNotFoundException, DepartmentAlreadyExistsException, AttributeKeyDoesNotExistException{
        attributes.forEach((k,v)->{
            DepartmentAttribute departmentAttribute = departmentAttributeRepository.findByAttributeKey(k).orElse(null);
            if(departmentAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

        Department newDepartment = new Department();

        Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(() -> new EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
        newDepartment.setManager(manager);
        if(createDepartmentDTO.getOrganizationId() != null) {
            Department existingDepartmentByName = departmentRepository.findByDepartmentAndOrganizationId(createDepartmentDTO.getDepartment(), createDepartmentDTO.getOrganizationId()).orElse(null);
            if(existingDepartmentByName != null){
                throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
            }
        }

        if(createDepartmentDTO.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(createDepartmentDTO.getOrganizationId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
            newDepartment.getOrganizations().add(organization);
            organization.getDepartments().add(newDepartment);
        }
        Map<DepartmentAttribute, String> departmentAttributes = attributes.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> departmentAttributeRepository.findByAttributeKey(entry.getKey())
                                .orElseThrow(() -> new RuntimeException("Attribute not found: " + entry.getKey())),
                        Map.Entry::getValue
                ));
        newDepartment.setDepartment(createDepartmentDTO.getDepartment());
        newDepartment.setDescription(createDepartmentDTO.getDescription());
        newDepartment.setAttributes(departmentAttributes);
        return departmentRepository.save(newDepartment);

    }

    public Department updateDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, Long departmentId)throws  DepartmentNotFoundException, EmployeeNotFoundException, DepartmentAlreadyExistsException{
        Department departmentFoundById = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

//        if (createDepartmentDTO.getOrganizationId() != null) {
//            if(!departmentFoundById.getOrganization().getOrganizationId().equals(createDepartmentDTO.getOrganizationId())) {
//                Department sameDepartmentInOtherOrganization = departmentRepository.findByDepartmentAndOrganization(createDepartmentDTO.getDepartment(), createDepartmentDTO.getOrganizationId()).orElse(null);
//                if (sameDepartmentInOtherOrganization != null) {
//                    throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
//                }
//            }
//        }
//
//        Department existingDepartmentByName = departmentRepository.findByDepartmentAndOrganization(createDepartmentDTO.getDepartment(), departmentFoundById.getOrganization().getOrganizationId()).orElse(null);
//
//       if (createDepartmentDTO.getOrganizationId() != null) {
//            if(!departmentFoundById.getOrganization().getOrganizationId().equals(createDepartmentDTO.getOrganizationId())) {
//                Department sameDepartmentInOtherOrganization = departmentRepository.findByDepartmentAndOrganization(createDepartmentDTO.getDepartment(), createDepartmentDTO.getOrganizationId()).orElse(null);
//                if (sameDepartmentInOtherOrganization != null) {
//                    throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
//                }
//            }
//        }
//        if(existingDepartmentByName != null && !departmentFoundById.getDepartment().equals(createDepartmentDTO.getDepartment())){
//            throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
//        }

        if(createDepartmentDTO.getDepartment() != null){
            departmentFoundById.setDepartment(createDepartmentDTO.getDepartment());
        }
        if(createDepartmentDTO.getDescription() != null){
            departmentFoundById.setDescription(createDepartmentDTO.getDescription());
        }
        if(createDepartmentDTO.getManagerId() != null){
            Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(()-> new EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
            departmentFoundById.setManager(manager);
        }
        if(createDepartmentDTO.getOrganizationId() != null){
            Organization organization = organizationRepository.findById(createDepartmentDTO.getOrganizationId()).orElseThrow(()-> new EntityNotFoundException("Organization not found"));
//            departmentFoundById.setOrganization(organization);
        }
        return departmentRepository.save(departmentFoundById);
    }

    @Transactional
    public void deleteDepartment(Long departmentId)throws DepartmentNotFoundException{
        Department departmentToDelete = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

//        if (departmentToDelete.getOrganization() != null) {
//            departmentToDelete.getOrganization().getDepartments().remove(departmentToDelete);
//            organizationRepository.save(departmentToDelete.getOrganization());
//        }

        List<Employee> employees = employeeRepository.findByDepartment(departmentToDelete);
        for (Employee employee : employees) {
            employee.setDepartment(null);
            employeeRepository.save(employee);
        }
        departmentRepository.delete(departmentToDelete);

    }

    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(Long departmentId) {
            super("Department with id " + departmentId+ " not found");
        }
    }

    public static class DepartmentAlreadyExistsException extends Exception {
        public DepartmentAlreadyExistsException(String department) {
            super("Department by name " + department+ " already exists.");
        }
    }
}
