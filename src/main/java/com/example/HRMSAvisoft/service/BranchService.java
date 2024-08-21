package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.example.HRMSAvisoft.dto.CreateBranchDTO;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;
import com.example.HRMSAvisoft.repository.BranchAttributeRepository;
import com.example.HRMSAvisoft.repository.BranchRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;

    private final OrganizationRepository organizationRepository;

    private final BranchAttributeRepository branchAttributesRepository;

    BranchService(BranchRepository branchRepository, OrganizationRepository organizationRepository, BranchAttributeRepository branchAttributeRepository) {
        this.branchRepository = branchRepository;
        this.organizationRepository = organizationRepository;
        this.branchAttributesRepository = branchAttributeRepository;
    }

    public Branch addBranch(CreateBranchDTO createBranchDTO) throws AttributeKeyDoesNotExistException, BranchAlreadyExistsException{

        createBranchDTO.getAttributes().forEach((k,v)->{
            BranchAttribute branchAttribute = branchAttributesRepository.findByAttributeKey(k).orElse(null);
            if(branchAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

        Branch newBranch = new Branch();
        if(createBranchDTO.getOrganizationId() != null){
            Branch existingBranchByName = branchRepository.findBranchByName(createBranchDTO.getBranchName()).orElse(null);
            if(existingBranchByName != null){
                throw new BranchAlreadyExistsException(createBranchDTO.getBranchName());
            }
        }
        newBranch.setBranchName(createBranchDTO.getBranchName());
        Organization organizationForBranch = organizationRepository.findById(createBranchDTO.getOrganizationId()).orElse(null);
        if(organizationForBranch != null) {
            organizationForBranch.getBranches().add(newBranch);
            newBranch.getOrganizations().add(organizationForBranch);
        }

        Map<BranchAttribute, String> branchAttributes = createBranchDTO.getAttributes().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> branchAttributesRepository.findByAttributeKey(entry.getKey())
                                .orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute not found: " + entry.getKey())),
                        Map.Entry::getValue
                ));

        newBranch.setAttributes(branchAttributes);

        return branchRepository.save(newBranch);
    }

    public List<Branch> getAllBranches(){
        return branchRepository.findAll();
    }

    public void updateBranch(CreateBranchDTO createBranchDTO, Long branchId)throws BranchAlreadyExistsException, EntityNotFoundException{
        createBranchDTO.getAttributes().forEach((k,v)->{
            BranchAttribute branchAttribute = branchAttributesRepository.findByAttributeKey(k).orElse(null);
            if(branchAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

        Branch branchFoundById = branchRepository.findById(branchId).orElseThrow(()-> new EntityNotFoundException("Branch not found."));
        Branch existingBranchByName = branchRepository.findBranchByName(createBranchDTO.getBranchName()).orElse(null);
        if(existingBranchByName != null && existingBranchByName.getBranchId() != branchId){
            throw new BranchAlreadyExistsException(createBranchDTO.getBranchName());
        }

        if(createBranchDTO.getBranchName() != null){
            branchFoundById.setBranchName(createBranchDTO.getBranchName());
        }

        Map<BranchAttribute, String> attributeMap = new HashMap<>();

        for (Map.Entry<String, String> entry : createBranchDTO.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            BranchAttribute branchAttribute = branchAttributesRepository.findByAttributeKey(key)
                    .orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute " + key + " does not exist"));
            attributeMap.put(branchAttribute, value);
        }
        Map<BranchAttribute, String> existingAttributes = branchFoundById.getAttributes();
        existingAttributes.putAll(attributeMap);

        branchRepository.save(existingBranchByName);

    }

    public void deleteBranch(Long branchId)throws EntityNotFoundException{
        Branch branchFoundById = branchRepository.findById(branchId).orElseThrow(()-> new EntityNotFoundException("Branach not found."));

        for(Organization organization : branchFoundById.getOrganizations()){
            if(organization.getBranches().contains(branchFoundById)){
                organization.getBranches().remove(branchFoundById);
            }
        }
        branchRepository.delete(branchFoundById);
    }

    public void assignBranchToOrganization(Long organizationId, Long branchId)throws EntityNotFoundException{
        Organization organizationToAddBranch = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        Branch branchToAdd = branchRepository.findById(branchId).orElseThrow(()-> new EntityNotFoundException("Branch not found"));

        if(!organizationToAddBranch.getBranches().contains(branchToAdd)){
            organizationToAddBranch.getBranches().add(branchToAdd);
        }

        if(!branchToAdd.getOrganizations().contains(organizationToAddBranch)){
            branchToAdd.getOrganizations().add(organizationToAddBranch);
        }

        branchRepository.save(branchToAdd);
    }

    public void removeBranchFromOrganization(Long organizationId, Long branchId)throws EntityNotFoundException{
        Branch branchToRemove = branchRepository.findById(branchId).orElseThrow(()-> new EntityNotFoundException("Branch not found"));

        Organization organizationToRemoveFrom = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        if(organizationToRemoveFrom.getDepartments().contains(branchToRemove)){
            organizationToRemoveFrom.getDepartments().remove(branchToRemove);

        }
        if(branchToRemove.getOrganizations().contains(organizationToRemoveFrom)) {
            branchToRemove.getOrganizations().remove(organizationToRemoveFrom);
        }

        branchRepository.save(branchToRemove);
    }



    public static class BranchAlreadyExistsException extends Exception {
        public BranchAlreadyExistsException(String branchName){
            super(branchName+" already exists.");
        }
    }


}
