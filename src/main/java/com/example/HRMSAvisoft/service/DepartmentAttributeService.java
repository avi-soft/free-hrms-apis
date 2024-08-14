package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.repository.DepartmentAttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class DepartmentAttributeService {

    private final DepartmentAttributeRepository departmentAttributeRepository;

    DepartmentAttributeService(DepartmentAttributeRepository departmentAttributeRepository) {
        this.departmentAttributeRepository = departmentAttributeRepository;
    }
    public List<DepartmentAttribute> getDepartmentAttributes(){
        return departmentAttributeRepository.findAll();
    }

    public DepartmentAttribute addDepartmentAttribute(DepartmentAttribute departmentAttribute)throws  DepartmentAlreadyExistsException{
        DepartmentAttribute existingDepartmentAttribute = departmentAttributeRepository.findByAttributeKey(departmentAttribute.getAttributeKey()).orElse(null);
        if (existingDepartmentAttribute != null) {
            throw new com.example.HRMSAvisoft.service.DepartmentAttributeService.DepartmentAlreadyExistsException(existingDepartmentAttribute.getAttributeKey() + " Department attrubute already exists");
        }
        return departmentAttributeRepository.save(departmentAttribute);
    }

    public DepartmentAttribute updateDepartmentAttribute(DepartmentAttribute departmentAttribute, Long departmentAttributeId)throws DepartmentAlreadyExistsException{
        DepartmentAttribute departmentAttributeToUpdate = departmentAttributeRepository.findById(departmentAttributeId).orElseThrow((() -> new EntityNotFoundException("Department Attribute not found")));

        DepartmentAttribute existingDepartmentAttribute = departmentAttributeRepository.findByAttributeKey(departmentAttribute.getAttributeKey()).orElse(null);
        if (existingDepartmentAttribute != null && !Objects.equals(existingDepartmentAttribute.getAttributeId(), departmentAttributeId)) {
            throw new com.example.HRMSAvisoft.service.DepartmentAttributeService.DepartmentAlreadyExistsException(existingDepartmentAttribute.getAttributeKey() + " department attribute already exists");
        }

        if (Objects.nonNull(departmentAttribute.getAttributeKey())) {
            departmentAttributeToUpdate.setAttributeKey(departmentAttribute.getAttributeKey());
        }
        return departmentAttributeRepository.save(departmentAttributeToUpdate);
    }

    @Transactional
    public DepartmentAttribute deleteDepartmentAttribute(Long departmentAttributeId) throws EntityNotFoundException {
        DepartmentAttribute departmentAttributeToDelete = departmentAttributeRepository.findById(departmentAttributeId).orElseThrow((() -> new EntityNotFoundException(departmentAttributeId + "not found")));
        departmentAttributeRepository.delete(departmentAttributeToDelete);
        return departmentAttributeToDelete;
    }

    public static class DepartmentAlreadyExistsException extends Exception {
        public DepartmentAlreadyExistsException(String message){
            super(message+ " department already exists");
        }
    }
}
