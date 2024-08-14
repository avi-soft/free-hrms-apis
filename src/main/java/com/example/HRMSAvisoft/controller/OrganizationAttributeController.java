package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.service.OrganizationAttributeService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utils.ResponseGenerator;

import java.util.List;
@RestController
@RequestMapping("/api/v1/organizationAttribute")
public class OrganizationAttributeController
{
    private final ModelMapper modelMapper;

    private final OrganizationAttributeService organizationAttributeService;

    OrganizationAttributeController(ModelMapper modelMapper, OrganizationAttributeService organizationAttributeService){
        this.modelMapper = modelMapper;
        this.organizationAttributeService = organizationAttributeService;
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_ORGANIZATION_ATTRIBUTES')")
    @GetMapping("")
    public ResponseEntity<List<OrganizationAttribute>> getOrganizationAttributes(){
        List<OrganizationAttribute> organizationAttributes = organizationAttributeService.getOrganizationAttributes();
        return ResponseEntity.status(HttpStatus.OK).body(organizationAttributes);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_ORGANIZATION_ATTRIBUTE')")
    @PostMapping("")
    public ResponseEntity<Object> saveOrganizationAttribute( @RequestBody OrganizationAttribute organizationAttribute) throws OrganizationAttributeService.OrganizationAttributeAlreadyExistsException, IllegalArgumentException {
        OrganizationAttribute organizationAttributeAdded = organizationAttributeService.addOrganizationAttribute(organizationAttribute);
        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"OrganizationAttribute is created successfully",organizationAttributeAdded);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_ORGANIZATION_ATTRIBUTE')")
    @PatchMapping("/{organizationAttributeId}")
    public ResponseEntity<Object> updateOrganizationAttribute( @RequestBody OrganizationAttribute organizationAttribute, @PathVariable Long organizationAttributeId) throws EntityNotFoundException, IllegalArgumentException{
        OrganizationAttribute updatedOrganizationAttribute = organizationAttributeService.updateOrganizationAttribute(organizationAttribute, organizationAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "OrganizationAttribute Updated successfully.",updatedOrganizationAttribute);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_ORGANIZATION_ATTRIBUTE')")
    @DeleteMapping("/{organizationAttributeId}")
    public ResponseEntity<Object> deleteOrganizationAttribute(@PathVariable Long organizationAttributeId) throws EntityNotFoundException
    {
        OrganizationAttribute deletedOrganizationAttribute=organizationAttributeService.deleteOrganizationAttribute(organizationAttributeId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"OrganizationAttribute is Deleted successfully",deletedOrganizationAttribute);
    }

    @ExceptionHandler({
            OrganizationAttributeService.OrganizationAttributeAlreadyExistsException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof OrganizationAttributeService.OrganizationAttributeAlreadyExistsException) {
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


