package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;
    private Cloudinary cloudinary;

    OrganizationService(OrganizationRepository organizationRepository, ModelMapper modelMapper, Cloudinary cloudinary) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    public void uploadOrganizationImage(Long organizationId, MultipartFile file)throws EntityNotFoundException, IOException, NullPointerException, RuntimeException , AccessDeniedException {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new EntityNotFoundException("No such organization"));

        // Upload file to Cloudinary
        Map<?, ?> params = ObjectUtils.asMap(
                "public_id", "profile_images/" + organizationId, // You can change the public_id format as you need
                "folder", "profile_images" // Optional: folder in Cloudinary to organize your images
        );
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

        // Get the URL of the uploaded image
        String imageUrl = (String) uploadResult.get("secure_url");

        // Set the image URL to the employee object and save it
        organization.setOrganizationImage(imageUrl);
        organizationRepository.save(organization);
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
        if(existingOrganization != null && !Objects.equals(existingOrganization.getOrganizationId(), organizationId)){
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

