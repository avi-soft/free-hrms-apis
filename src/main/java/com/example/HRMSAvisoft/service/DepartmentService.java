package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Transactional
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    private final OrganizationRepository organizationRepository;

    DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<Department> getAllDepartments(Long organizationId) {
        return departmentRepository.findAllByOrganizationId(organizationId);
    }

    public Department addDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, Long organizationId) throws EmployeeNotFoundException, EntityNotFoundException, DepartmentAlreadyExistsException{
        Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(()-> new EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        Department newDepartment = new Department();
        Department existingDepartmentByName = departmentRepository.findByDepartmentAndOrganization(createDepartmentDTO.getDepartment(), organizationId).orElse(null);
        if(existingDepartmentByName != null){
            throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
        }
        newDepartment.setDepartment(createDepartmentDTO.getDepartment());
        newDepartment.setDescription(createDepartmentDTO.getDescription());
        newDepartment.setManager(manager);
        newDepartment.setOrganization(organization);
        organization.getDepartments().add(newDepartment);
        return departmentRepository.save(newDepartment);
    }

    public Department updateDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, Long departmentId)throws  DepartmentNotFoundException, EmployeeNotFoundException, DepartmentAlreadyExistsException{
        Department departmentFoundById = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        Department existingDepartmentByName = departmentRepository.findByDepartmentAndOrganization(createDepartmentDTO.getDepartment(), departmentFoundById.getOrganization().getOrganizationId()).orElse(null);

        if(existingDepartmentByName != null && !departmentFoundById.getDepartment().equals(createDepartmentDTO.getDepartment())){
            throw new DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
        }

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
            departmentFoundById.setOrganization(organization);
        }
        return departmentRepository.save(departmentFoundById);
    }

    @Transactional
    public void deleteDepartment(Long departmentId)throws DepartmentNotFoundException{
        Department departmentToDelete = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        if (departmentToDelete.getOrganization() != null) {
            departmentToDelete.getOrganization().getDepartments().remove(departmentToDelete);
            organizationRepository.save(departmentToDelete.getOrganization());
        }

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
