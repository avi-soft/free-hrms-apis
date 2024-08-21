package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.exception.AttributeKeyAlreadyExistsException;
import com.example.HRMSAvisoft.repository.BranchAttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BranchAttributeService {

    private final BranchAttributeRepository branchAttributeRepository;

    BranchAttributeService(BranchAttributeRepository branchAttributeRepository){
        this.branchAttributeRepository = branchAttributeRepository;
    }
    public BranchAttribute addBranchAttribute(BranchAttribute branchAttribute)throws AttributeKeyAlreadyExistsException{
        BranchAttribute existingBranchAttributeByKey = branchAttributeRepository.findByAttributeKey(branchAttribute.getAttributeKey()).orElse(null);
        if(existingBranchAttributeByKey != null){
            throw new AttributeKeyAlreadyExistsException(branchAttribute.getAttributeKey());
        }
        return branchAttributeRepository.save(branchAttribute);
    }

    public List<BranchAttribute> getBranchAttributes(){
        return branchAttributeRepository.findAll();
    }

    public BranchAttribute updateBranchAttribute(BranchAttribute branchAttribute, Long branchAttributeId)throws AttributeKeyAlreadyExistsException{
        BranchAttribute branchAttributeToUpdate = branchAttributeRepository.findById(branchAttributeId).orElseThrow((() -> new EntityNotFoundException("Department Attribute not found")));

        BranchAttribute existingBranchAttribute = branchAttributeRepository.findByAttributeKey(branchAttribute.getAttributeKey()).orElse(null);
        if (existingBranchAttribute != null && !Objects.equals(existingBranchAttribute.getAttributeId(), branchAttributeId)) {
            throw new AttributeKeyAlreadyExistsException(existingBranchAttribute.getAttributeKey());
        }

        if (Objects.nonNull(branchAttribute.getAttributeKey())) {
            branchAttributeToUpdate.setAttributeKey(branchAttribute.getAttributeKey());
        }
        return branchAttributeRepository.save(branchAttributeToUpdate);
    }

    public void deleteBranchAttribute(Long branchAttributeId)throws EntityNotFoundException{
        BranchAttribute branchAttributeToDelete = branchAttributeRepository.findById(branchAttributeId).orElseThrow((() -> new EntityNotFoundException(branchAttributeId + " branch not found")));
        branchAttributeRepository.delete(branchAttributeToDelete);
    }
}
