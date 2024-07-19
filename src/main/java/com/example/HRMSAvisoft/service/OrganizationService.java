package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;

    OrganizationService(OrganizationRepository organizationRepository,ModelMapper modelMapper) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
    }

    public List<Organization> getOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizations;
    }

    public Organization addOrganization(AddNewOrganizationDTO organizationDTO) throws OrganizationAlreadyExistsException, IllegalArgumentException{
        Organization organizationToAdd= modelMapper.map(organizationDTO,Organization.class);
        Organization existingOrganization = organizationRepository.getByOrganizationName(organizationDTO.getOrganizationName()).orElse(null);
        if(existingOrganization != null){
            throw new OrganizationAlreadyExistsException(existingOrganization.getOrganizationName() + " organization already exists");
        }
        return organizationRepository.save(organizationToAdd);
    }

    public Organization updateOrganization(UpdateOrganizationDTO organizationDTO, Long organizationId) throws EntityNotFoundException, IllegalArgumentException{
        Organization organizationToUpdate = organizationRepository.findById(organizationId).orElseThrow((()-> new EntityNotFoundException("Organization not found")));

        Organization existingOrganization = organizationRepository.getByOrganizationName(organizationDTO.getOrganizationName()).orElse(null);
        if(existingOrganization != null){
            throw new OrganizationAlreadyExistsException(existingOrganization.getOrganizationName() + " organization already exists");
        }

        if (Objects.nonNull(organizationDTO.getOrganizationName())) {
            organizationToUpdate.setOrganizationName(organizationDTO.getOrganizationName());
        }
        if (Objects.nonNull(organizationDTO.getOrganizationDescription())) {
            organizationToUpdate.setOrganizationDescription(organizationDTO.getOrganizationDescription());
        }
        return organizationRepository.save(organizationToUpdate);
    }

    @Transactional
    public Organization deleteOrganization(Long organizationId) throws EntityNotFoundException {
        Organization organizationToDelete = organizationRepository.findById(organizationId).orElseThrow((()-> new EntityNotFoundException(organizationId+ "not found")));
        organizationRepository.delete(organizationToDelete);
        return organizationToDelete;
    }

    public static class OrganizationAlreadyExistsException extends RuntimeException{
        public OrganizationAlreadyExistsException(String message) {
            super(message);
        }
    }
}

