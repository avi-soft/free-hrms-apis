package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.repository.OrganizationAttributeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class OrganizationAttributeService{

    private final OrganizationAttributeRepository organizationAttributeRepository;
    private final OrganizationRepository organizationRepository;
    private static final String ATTRIBUTE_KEY_REGEX = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";

    OrganizationAttributeService(OrganizationAttributeRepository organizationAttributeRepository, OrganizationRepository organizationRepository) {
        this.organizationAttributeRepository = organizationAttributeRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<OrganizationAttribute> getOrganizationAttributes() {
        List<OrganizationAttribute> organizationAttributes = organizationAttributeRepository.findAll();
        return organizationAttributes;
    }

    public OrganizationAttribute addOrganizationAttribute(OrganizationAttribute organizationAttribute)
            throws com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException, IllegalArgumentException {

        // Check if the attribute key already exists
        OrganizationAttribute existingOrganizationAttribute = organizationAttributeRepository.findByAttributeKey(organizationAttribute.getAttributeKey()).orElse(null);
        if (existingOrganizationAttribute != null) {
            throw new com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException(existingOrganizationAttribute.getAttributeKey() + " organization attribute already exists.");
        }

        // Validate the attribute key
        if (organizationAttribute.getAttributeKey() != null && !organizationAttribute.getAttributeKey().trim().isEmpty()) {
            if (!organizationAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Key cannot contain numbers or special characters");
            }
            return organizationAttributeRepository.save(organizationAttribute);
        } else {
            throw new IllegalArgumentException("key cannot be empty.");
        }
    }

    public OrganizationAttribute updateOrganizationAttribute(OrganizationAttribute organizationAttribute, Long organizationAttributeId)
            throws EntityNotFoundException, IllegalArgumentException {


        // Find the existing organization attribute by ID
        OrganizationAttribute organizationAttributeToUpdate = organizationAttributeRepository.findById(organizationAttributeId)
                .orElseThrow(() -> new EntityNotFoundException("Organization attribute not found."));

        // Check if an attribute with the same key already exists (but is not the current one)
        OrganizationAttribute existingOrganizationAttribute = organizationAttributeRepository.findByAttributeKey(organizationAttribute.getAttributeKey()).orElse(null);
        if (existingOrganizationAttribute != null && !Objects.equals(existingOrganizationAttribute.getAttributeId(), organizationAttributeId)) {
            throw new com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException(existingOrganizationAttribute.getAttributeKey() + " organization attribute already exists.");
        }

        // Validate and update the attribute key
        if (Objects.nonNull(organizationAttribute.getAttributeKey()) && !organizationAttribute.getAttributeKey().trim().isEmpty()) {
            if (!organizationAttribute.getAttributeKey().trim().matches(ATTRIBUTE_KEY_REGEX)) {
                throw new IllegalArgumentException("Key cannot contain numbers or special characters");
            }
            organizationAttributeToUpdate.setAttributeKey(organizationAttribute.getAttributeKey());
        } else {
            throw new IllegalArgumentException("key cannot be empty.");
        }

        // Save and return the updated organization attribute
        return organizationAttributeRepository.save(organizationAttributeToUpdate);
    }

    @Transactional
    public OrganizationAttribute deleteOrganizationAttribute(Long organizationAttributeId) throws EntityNotFoundException {
        OrganizationAttribute organizationAttributeToDelete = organizationAttributeRepository.findById(organizationAttributeId).orElseThrow((() -> new EntityNotFoundException(organizationAttributeId + "not found")));

        List<Organization> organizationList = organizationRepository.findAll();
        for(Organization organization : organizationList){
            organization.getAttributes().forEach((k, v)->{
                if(k.equals(organizationAttributeToDelete)){
                    organization.getAttributes().remove(organizationAttributeToDelete);
                }
            });
        }

        organizationAttributeRepository.delete(organizationAttributeToDelete);
        return organizationAttributeToDelete;
    }

    public static class OrganizationAttributeAlreadyExistsException extends RuntimeException {
        public OrganizationAttributeAlreadyExistsException(String message) {
            super(message);
        }
    }
}



