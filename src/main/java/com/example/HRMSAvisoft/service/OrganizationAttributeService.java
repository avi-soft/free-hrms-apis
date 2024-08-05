package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import com.example.HRMSAvisoft.repository.OrganizationAttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class OrganizationAttributeService{

    private final OrganizationAttributeRepository organizationAttributeRepository;
    private final ModelMapper modelMapper;
    private Cloudinary cloudinary;

    OrganizationAttributeService(OrganizationAttributeRepository organizationAttributeRepository, ModelMapper modelMapper, Cloudinary cloudinary) {
        this.organizationAttributeRepository = organizationAttributeRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    public List<OrganizationAttribute> getOrganizationAttributes() {
        List<OrganizationAttribute> organizationAttributes = organizationAttributeRepository.findAll();
        return organizationAttributes;
    }

    public OrganizationAttribute addOrganizationAttribute(OrganizationAttribute organizationAttribute) throws com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException, IllegalArgumentException {
        OrganizationAttribute existingOrganizationAttribute = organizationAttributeRepository.findByAttributeKey(organizationAttribute.getAttributeKey()).orElse(null);
        if (existingOrganizationAttribute != null) {
            throw new com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException(existingOrganizationAttribute.getAttributeKey() + " organizationAttribute already exists");
        }
        return organizationAttributeRepository.save(organizationAttribute);
    }

    public OrganizationAttribute updateOrganizationAttribute(OrganizationAttribute organizationAttribute, Long organizationAttributeId) throws EntityNotFoundException, IllegalArgumentException {
        OrganizationAttribute organizationAttributeToUpdate = organizationAttributeRepository.findById(organizationAttributeId).orElseThrow((() -> new EntityNotFoundException("OrganizationAttribute not found")));

        OrganizationAttribute existingOrganizationAttribute = organizationAttributeRepository.findByAttributeKey(organizationAttribute.getAttributeKey()).orElse(null);
        if (existingOrganizationAttribute != null && !Objects.equals(existingOrganizationAttribute.getAttributeId(), organizationAttributeId)) {
            throw new com.example.HRMSAvisoft.service.OrganizationAttributeService.OrganizationAttributeAlreadyExistsException(existingOrganizationAttribute.getAttributeKey() + " organizationAttribute already exists");
        }
        if (Objects.nonNull(organizationAttribute.getAttributeKey())) {
            organizationAttributeToUpdate.setAttributeKey(organizationAttribute.getAttributeKey());
        }
        return organizationAttributeRepository.save(organizationAttributeToUpdate);
    }

    @Transactional
    public OrganizationAttribute deleteOrganizationAttribute(Long organizationAttributeId) throws EntityNotFoundException {
        OrganizationAttribute organizationAttributeToDelete = organizationAttributeRepository.findById(organizationAttributeId).orElseThrow((() -> new EntityNotFoundException(organizationAttributeId + "not found")));
        organizationAttributeRepository.delete(organizationAttributeToDelete);
        return organizationAttributeToDelete;
    }

    public static class OrganizationAttributeAlreadyExistsException extends RuntimeException {
        public OrganizationAttributeAlreadyExistsException(String message) {
            super(message);
        }
    }
}



