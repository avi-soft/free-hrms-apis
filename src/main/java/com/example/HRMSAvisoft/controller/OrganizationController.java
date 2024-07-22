package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddNewOrganizationDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.dto.UpdateOrganizationDTO;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.OrganizationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.ResponseGenerator;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final ModelMapper modelMapper;

    private final OrganizationService organizationService;

    OrganizationController(ModelMapper modelMapper, OrganizationService organizationService){
        this.modelMapper = modelMapper;
        this.organizationService = organizationService;
    }

//    @PreAuthorize("hasAuthority('UPLOAD_ORGANIZATION_IMAGE')")
    @PostMapping("/{organizationId}/uploadImage")
    public ResponseEntity<String> uploadOrganizationImage(@PathVariable("organizationId") Long organizationId, @RequestParam("file") MultipartFile file) throws EmployeeNotFoundException, IOException, NullPointerException, RuntimeException , AccessDeniedException {
        organizationService.uploadOrganizationImage(organizationId, file);
        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
        return ResponseEntity.ok().body(message);
    }


    @PreAuthorize("hasAnyAuthority('GET_ALL_ORGANIZATIONS')")
    @GetMapping("")
    public ResponseEntity<List<Organization>> getOrganizations(){
        List<Organization> organizations = organizationService.getOrganizations();
        return ResponseEntity.status(HttpStatus.OK).body(organizations);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_ORGANIZATION')")
    @PostMapping("")
    public ResponseEntity<Object> saveOrganization(@Valid @RequestBody AddNewOrganizationDTO organizationDTO) throws OrganizationService.OrganizationAlreadyExistsException, IllegalArgumentException {
        Organization organizationAdded = organizationService.addOrganization(organizationDTO);
        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Organization is created successfully",organizationAdded);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_ORGANIZATION')")
    @PatchMapping("/{organizationId}")
    public ResponseEntity<Object> updateOrganization(@Valid @RequestBody UpdateOrganizationDTO organizationDTO, @PathVariable Long organizationId) throws EntityNotFoundException, IllegalArgumentException{
        Organization updatedOrganization = organizationService.updateOrganization(organizationDTO, organizationId);
        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "Organization Updated successfully.",updatedOrganization);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_ORGANIZATION')")
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Object> deleteOrganization(@PathVariable Long organizationId) throws EntityNotFoundException
    {
        Organization deletedOrganization=organizationService.deleteOrganization(organizationId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"Organization is Deleted successfully",deletedOrganization);
    }

    @ExceptionHandler({
            OrganizationService.OrganizationAlreadyExistsException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof OrganizationService.OrganizationAlreadyExistsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof IllegalArgumentException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else {
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}

