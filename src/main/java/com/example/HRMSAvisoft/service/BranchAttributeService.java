package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.exception.AttributeKeyAlreadyExistsException;
import com.example.HRMSAvisoft.repository.BranchAttributeRepository;
import com.example.HRMSAvisoft.repository.BranchRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BranchAttributeService {

    private final BranchAttributeRepository branchAttributeRepository;

    private final BranchRepository branchRepository;

    private static final String ATTRIBUTE_KEY_REGEX = "^[a-zA-Z]+( [a-zA-Z]+)*$";

    BranchAttributeService(BranchAttributeRepository branchAttributeRepository, BranchRepository branchRepository){
        this.branchAttributeRepository = branchAttributeRepository;
        this.branchRepository = branchRepository;
    }
    public BranchAttribute addBranchAttribute(BranchAttribute branchAttribute)
            throws AttributeKeyAlreadyExistsException, IllegalArgumentException {

        // Check if an attribute with the same key already exists
        BranchAttribute existingBranchAttributeByKey = branchAttributeRepository.findByAttributeKey(branchAttribute.getAttributeKey()).orElse(null);
        if (existingBranchAttributeByKey != null) {
            throw new AttributeKeyAlreadyExistsException(branchAttribute.getAttributeKey() + " attribute key already exists.");
        }

        // Validate the attribute key
        if (branchAttribute.getAttributeKey() != null && !branchAttribute.getAttributeKey().trim().isEmpty()) {
            if (!branchAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters");
            }
            return branchAttributeRepository.save(branchAttribute);
        } else {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }
    }

    public List<BranchAttribute> getBranchAttributes() {
        return branchAttributeRepository.findAll();
    }

    public BranchAttribute updateBranchAttribute(BranchAttribute branchAttribute, Long branchAttributeId)
            throws AttributeKeyAlreadyExistsException, IllegalArgumentException {


        // Find the branch attribute to update
        BranchAttribute branchAttributeToUpdate = branchAttributeRepository.findById(branchAttributeId)
                .orElseThrow(() -> new EntityNotFoundException("Branch Attribute not found"));

        // Check if an attribute with the same key already exists (but is not the current one)
        BranchAttribute existingBranchAttribute = branchAttributeRepository.findByAttributeKey(branchAttribute.getAttributeKey()).orElse(null);
        if (existingBranchAttribute != null && !Objects.equals(existingBranchAttribute.getAttributeId(), branchAttributeId)) {
            throw new AttributeKeyAlreadyExistsException(existingBranchAttribute.getAttributeKey() + " attribute key already exists.");
        }

        // Validate and update the attribute key
        if (Objects.nonNull(branchAttribute.getAttributeKey()) && !branchAttribute.getAttributeKey().trim().isEmpty()) {
            if (!branchAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Attribute name cannot contain numbers or special characters");
            }
            branchAttributeToUpdate.setAttributeKey(branchAttribute.getAttributeKey());
        } else {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }

        // Save and return the updated branch attribute
        return branchAttributeRepository.save(branchAttributeToUpdate);
    }


    public void deleteBranchAttribute(Long branchAttributeId)throws EntityNotFoundException{
        BranchAttribute branchAttributeToDelete = branchAttributeRepository.findById(branchAttributeId).orElseThrow((() -> new EntityNotFoundException(branchAttributeId + " branch not found")));

        List<Branch> branchList = branchRepository.findAll();
        for(Branch branch : branchList){
            branch.getAttributes().forEach((k, v)->{
                if(k.equals(branchAttributeToDelete)){
                    branch.getAttributes().remove(branchAttributeToDelete);
                }
            });
        }

        branchAttributeRepository.delete(branchAttributeToDelete);
    }
}
