package com.example.HRMSAvisoft.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.AttributeKeyDoesNotExistException;

import com.example.HRMSAvisoft.repository.OrganizationAttributeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB in bytes
    private static final List<String> ACCEPTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;
    private Cloudinary cloudinary;
    private OrganizationAttributeRepository organizationAttributeRepository;

    OrganizationService(OrganizationRepository organizationRepository, ModelMapper modelMapper, Cloudinary cloudinary, OrganizationAttributeRepository organizationAttributeRepository) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
        this.organizationAttributeRepository = organizationAttributeRepository;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Organization addOrganizationWithImage(AddNewOrganizationDTO organizationDTO, MultipartFile file)
            throws OrganizationAlreadyExistsException, ValidationException, IOException, EntityNotFoundException, IllegalArgumentException, MaxUploadSizeExceededException {
        // Step 1: Add organization
        Organization organizationAdded = addOrganization(organizationDTO);

        // Step 2: Upload organization image if the file is present
        if (file != null && !file.isEmpty()) {
            // Validate the image
            validateImage(file);
            // Upload the image
            uploadOrganizationImage(organizationAdded.getOrganizationId(), file);
        }


        return organizationAdded;
    }

    // Validation method for file
    public void validateImage(MultipartFile file) throws jakarta.validation.ValidationException {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new jakarta.validation.ValidationException("File is empty. Please upload a valid image.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MaxUploadSizeExceededException(file.getSize());
        }

        // Check file content type (e.g., only JPEG, PNG)
        if (!ACCEPTED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new jakarta.validation.ValidationException("Invalid file type. Only JPEG and PNG files are allowed.");
        }
    }

    public void uploadOrganizationImage(Long organizationId, MultipartFile file)throws EntityNotFoundException, IOException, RuntimeException {
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

    public void removeOrganizationImage(Long organizationId) throws EntityNotFoundException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("No such organization"));

        organization.setOrganizationImage(null);
        organizationRepository.save(organization);

    }

    public Page<Department> getDepartmentsOfOrganization(Long organizationId, int page, int size) throws EntityNotFoundException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        Pageable pageable = PageRequest.of(page, size);
        List<Department> departments = organization.getDepartments().stream().toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departments.size());
        return new PageImpl<>(departments.subList(start, end), pageable, departments.size());
    }


    public List<Branch> getBranchesOfOrganization(Long organizationId)throws EntityNotFoundException{
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));

        return organization.getBranches().stream().toList();
    }


    public Page<Organization> getOrganizations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Organization> organizations = organizationRepository.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), organizations.size());

        return new PageImpl<>(organizations.subList(start, end), pageable, organizations.size());
    }

public Organization addOrganization(AddNewOrganizationDTO organizationDTO) throws OrganizationAlreadyExistsException, IllegalArgumentException {
    Organization organizationToAdd = modelMapper.map(organizationDTO, Organization.class);
    Organization existingOrganization = organizationRepository.getByOrganizationName(organizationDTO.getOrganizationName()).orElse(null);

    if (existingOrganization != null) {
        throw new OrganizationAlreadyExistsException(existingOrganization.getOrganizationName() + " organization already exists");
    }
    System.out.println( organizationDTO.getAttributes());

    Map<OrganizationAttribute, String> organizationAttributes = organizationDTO.getAttributes().entrySet().stream()
            .collect(Collectors.toMap(
                    entry -> organizationAttributeRepository.findByAttributeKey(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("Attribute not found: " + entry.getKey())),
                    Map.Entry::getValue
            ));

    organizationToAdd.setAttributes(organizationAttributes);
    return organizationRepository.save(organizationToAdd);
}

    public Organization updateOrganization(UpdateOrganizationDTO organizationDTO, Long organizationId) throws EntityNotFoundException, IllegalArgumentException, AttributeKeyDoesNotExistException{
        organizationDTO.getAttributes().forEach((k,v)->{
            OrganizationAttribute organizationAttribute = organizationAttributeRepository.findByAttributeKey(k).orElse(null);
            if(organizationAttribute == null){
                throw new AttributeKeyDoesNotExistException("Attribute "+ k + " does not exist");
            }
        });

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

        Map<OrganizationAttribute, String> attributeMap = new HashMap<>();

        for (Map.Entry<String, String> entry : organizationDTO.getAttributes().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            OrganizationAttribute organizationAttribute = organizationAttributeRepository.findByAttributeKey(key)
                    .orElseThrow(() -> new AttributeKeyDoesNotExistException("Attribute " + key + " does not exist"));
            attributeMap.put(organizationAttribute, value);
        }
        Map<OrganizationAttribute, String> existingAttributes = organizationToUpdate.getAttributes();
        existingAttributes.putAll(attributeMap);

        return organizationRepository.save(organizationToUpdate);
    }

    @Transactional
    public void deleteOrganization(Long organizationId) throws EntityNotFoundException {
        Organization organizationToDelete = organizationRepository.findById(organizationId).orElseThrow((()-> new EntityNotFoundException(organizationId+ "not found")));

        for(Department department : organizationToDelete.getDepartments()){
            if(department.getOrganizations().contains(organizationToDelete))
                department.getOrganizations().remove(organizationToDelete);
        }

        for(Branch branch : organizationToDelete.getBranches()){
            if(branch.getOrganizations().contains(organizationToDelete))
                branch.getOrganizations().remove(organizationToDelete);
        }
        organizationRepository.delete(organizationToDelete);
    }

    public static class OrganizationAlreadyExistsException extends RuntimeException{
        public OrganizationAlreadyExistsException(String message) {
            super(message);
        }
    }
    }


