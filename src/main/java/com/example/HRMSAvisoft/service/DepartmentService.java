package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    private final DepartmentAttributeRepository departmentAttributeRepository;

    private final OrganizationRepository organizationRepository;

    private final BranchRepository branchRepository;

    DepartmentService(DepartmentRepository departmentRepository, BranchRepository branchRepository, EmployeeRepository employeeRepository, DepartmentAttributeRepository departmentAttributeRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentAttributeRepository = departmentAttributeRepository;
        this.organizationRepository = organizationRepository;
        this.branchRepository = branchRepository;
    }

    public Page<Department> getAllDepartments(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Department> departments = departmentRepository.findAll();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departments.size());
        return new PageImpl<>(departments.subList(start, end), pageable, departments.size());
    }

    public Department addDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO) throws EmployeeNotFoundException, EntityNotFoundException, DepartmentAttributeService.DepartmentAlreadyExistsException, AttributeKeyDoesNotExistException{
        createDepartmentDTO.getAttributes().forEach((k,v)->{
            DepartmentAttribute departmentAttribute = departmentAttributeRepository.findByAttributeKey(k).orElse(null);
            if(departmentAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

        Department newDepartment = new Department();
        if(createDepartmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(() -> new EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
            newDepartment.setManager(manager);
        }

        if(createDepartmentDTO.getOrganizationId() != null) {
            Department existingDepartmentByName = departmentRepository.findByDepartmentAndOrganizationId(createDepartmentDTO.getDepartment(), createDepartmentDTO.getOrganizationId()).orElse(null);
            if(existingDepartmentByName != null){
                throw new DepartmentAttributeService.DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment());
            }
        }

        if(createDepartmentDTO.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(createDepartmentDTO.getOrganizationId()).orElseThrow(() -> new EntityNotFoundException("Organization not found"));
            newDepartment.getOrganizations().add(organization);
            organization.getDepartments().add(newDepartment);
        }
        Map<DepartmentAttribute, String> departmentAttributes = createDepartmentDTO.getAttributes().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> departmentAttributeRepository.findByAttributeKey(entry.getKey())
                                .orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute not found: " + entry.getKey())),
                        Map.Entry::getValue
                ));
        newDepartment.setDepartment(createDepartmentDTO.getDepartment());
        newDepartment.setDescription(createDepartmentDTO.getDescription());
        newDepartment.setAttributes(departmentAttributes);
        return departmentRepository.save(newDepartment);

    }

    public Department updateDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO, Long departmentId)throws  DepartmentNotFoundException, EmployeeNotFoundException, DepartmentAttributeService.DepartmentAlreadyExistsException {

        createDepartmentDTO.getAttributes().forEach((k,v)->{
            DepartmentAttribute departmentAttribute = departmentAttributeRepository.findByAttributeKey(k).orElse(null);
            if(departmentAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

        Department departmentFoundById = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));
        Department departmentByName = departmentRepository.findByDepartment(createDepartmentDTO.getDepartment()).orElse(null);

        if(departmentByName != null && departmentByName.getDepartmentId() != departmentId){
            throw new DepartmentAttributeService.DepartmentAlreadyExistsException(createDepartmentDTO.getDepartment()+ " already exists.");
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

        Map<DepartmentAttribute, String> attributeMap = new HashMap<>();

        for (Map.Entry<String, String> entry : createDepartmentDTO.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            DepartmentAttribute departmentAttribute = departmentAttributeRepository.findByAttributeKey(key)
                    .orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute " + key + " does not exist"));
            attributeMap.put(departmentAttribute, value);
        }
        Map<DepartmentAttribute, String> existingAttributes = departmentFoundById.getAttributes();
        existingAttributes.putAll(attributeMap);

        return departmentRepository.save(departmentFoundById);
    }

    public void deleteDepartment(Long departmentId)throws DepartmentNotFoundException{
        Department departmentToDelete = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        for (Employee employee : departmentToDelete.getEmployees()) {
            if(employee.getDepartments().contains(departmentToDelete)) {
                employee.getDepartments().remove(departmentToDelete);
            }
        }

        for(Branch branch : departmentToDelete.getBranches()){
            if(branch.getDepartments().contains(departmentToDelete))
                branch.getDepartments().remove(departmentToDelete);
        }

        for(Organization organization : departmentToDelete.getOrganizations()) {
            if(organization.getDepartments().contains(departmentToDelete))
                organization.getDepartments().remove(departmentToDelete);
        }

        departmentRepository.delete(departmentToDelete);
    }

    public void assignDepartmentToOrganization(Long organizationId, Long departmentId)throws EntityNotFoundException, DepartmentNotFoundException{
        Department departmentToAssign = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        Organization organizationToAddInto = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        if(!organizationToAddInto.getDepartments().contains(departmentToAssign)){
            organizationToAddInto.getDepartments().add(departmentToAssign);

        }
        if(!departmentToAssign.getOrganizations().contains(organizationToAddInto)) {
            departmentToAssign.getOrganizations().add(organizationToAddInto);
        }
        departmentRepository.save(departmentToAssign);
    }

    public void removeDepartmentFromOrganization(Long organizationId, Long departmentId)throws EntityNotFoundException, DepartmentNotFoundException{
        Department departmentToRemove = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        Organization organizationToRemoveFrom = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        if(organizationToRemoveFrom.getDepartments().contains(departmentToRemove)){
            organizationToRemoveFrom.getDepartments().remove(departmentToRemove);

        }
        if(departmentToRemove.getOrganizations().contains(organizationToRemoveFrom)) {
            departmentToRemove.getOrganizations().remove(organizationToRemoveFrom);
        }

        departmentRepository.save(departmentToRemove);

    }

    public void assignDepartmentToBranch(Long branchId, Long departmentId)throws EntityNotFoundException, DepartmentNotFoundException{
        Department departmentToAssign = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        Branch branchToaddInto = branchRepository.findById(branchId).orElseThrow(()->new EntityNotFoundException("Branch not found"));

        if(!branchToaddInto.getDepartments().contains(departmentToAssign)){
            branchToaddInto.getDepartments().add(departmentToAssign);

        }
        if(!departmentToAssign.getBranches().contains(branchToaddInto)) {
            departmentToAssign.getBranches().add(branchToaddInto);
        }
        departmentRepository.save(departmentToAssign);
    }

    public void removeDepartmentFromBranch(Long branchId, Long departmentId)throws EntityNotFoundException, DepartmentNotFoundException{
        Department departmentToRemove = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        Branch branchToRemoveFrom = branchRepository.findById(branchId).orElseThrow(()-> new EntityNotFoundException("Branch not found"));

        if(branchToRemoveFrom.getDepartments().contains(departmentToRemove)){
            branchToRemoveFrom.getDepartments().remove(departmentToRemove);

        }
        if(departmentToRemove.getBranches().contains(branchToRemoveFrom)) {
            departmentToRemove.getBranches().remove(branchToRemoveFrom);
        }

        departmentRepository.save(departmentToRemove);
    }

    public void assignEmployeeToDepartment(Long employeeId, Long departmentId)throws EntityNotFoundException{
        Employee employeeToAssign = employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Employee not found."));

        Department departmentToAssignInto = departmentRepository.findById(departmentId).orElseThrow(()-> new EntityNotFoundException("Department not found."));

        if(!employeeToAssign.getDepartments().contains(departmentToAssignInto)){
            employeeToAssign.getDepartments().add(departmentToAssignInto);
        }

        if(!departmentToAssignInto.getEmployees().contains(employeeToAssign))
            departmentToAssignInto.getEmployees().add(employeeToAssign);

        departmentRepository.save(departmentToAssignInto);
    }

    public void removeEmployeeFromDepartment(Long employeeId, Long departmentId)throws EntityNotFoundException{
        Employee employeeToRemove = employeeRepository.findById(employeeId).orElseThrow(()-> new EntityNotFoundException("Employee not found"));

        Department departmentToRemoveFrom = departmentRepository.findById(departmentId).orElseThrow(()->new EntityNotFoundException("Department not found"));

        if(employeeToRemove.getDepartments().contains(departmentToRemoveFrom))
            employeeToRemove.getDepartments().remove(departmentToRemoveFrom);
        if(departmentToRemoveFrom.getEmployees().contains(employeeToRemove))
            departmentToRemoveFrom.getEmployees().remove(employeeToRemove);

        departmentRepository.save(departmentToRemoveFrom);
    }

    public Page<Employee> getEmployeesOfDepartment(int page, int size, Long departmentId){
        Department department = departmentRepository.findById(departmentId).orElseThrow(()-> new EntityNotFoundException("Department not found"));

        Pageable pageable = PageRequest.of(page, size);

        List<Employee> employeesList = department.getEmployees().stream().toList();

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), employeesList.size());

        return new PageImpl<>(employeesList.subList(start, end), pageable, employeesList.size());

    }

    public Page<Department> getAllUnassignedDepartmentsOfBranch(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<Department> departments = departmentRepository.findAllDepartmentWhereBranchIsEmpty();

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departments.size());

        return new PageImpl<>(departments.subList(start, end), pageable, departments.size());
    }

    public Page<Department> getAllUnassignedDepartmentsOfOrganization(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        List<Department> departments = departmentRepository.findAllDepartmentWhereOrganizationIsEmpty();

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departments.size());

        return new PageImpl<>(departments.subList(start, end), pageable, departments.size());
    }





    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(Long departmentId) {
            super("Department with id " + departmentId+ " not found");
        }
    }

}