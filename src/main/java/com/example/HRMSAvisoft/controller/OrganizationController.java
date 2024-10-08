package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.*;
import com.example.HRMSAvisoft.entity.Branch;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.OrganizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import utils.ResponseGenerator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final OrganizationService organizationService;

    OrganizationController(ModelMapper modelMapper, OrganizationService organizationService){
        this.organizationService = organizationService;
    }

//    @PreAuthorize("hasAuthority('UPLOAD_ORGANIZATION_IMAGE')")
//    @PostMapping("/{organizationId}/uploadImage")
//    public ResponseEntity<String> uploadOrganizationImage(@PathVariable("organizationId") Long organizationId, @RequestParam("file") MultipartFile file) throws EntityNotFoundException, ValidationException, MaxUploadSizeExceededException, IOException, RuntimeException  {
//
//        // Validate the file
//        validateImage(file);
//
//        organizationService.uploadOrganizationImage(organizationId, file);
//        String message = "{\"message\": \"Profile Uploaded Successfully\"}";
//        return ResponseEntity.ok().body(message);
//    }

    @DeleteMapping("/{organizationId}/removeImage")
    public ResponseEntity<String> removeOrganizationImage(@PathVariable("organizationId") Long organizationId) throws EmployeeNotFoundException {
        organizationService.removeOrganizationImage(organizationId);
        String message = "{\"message\": \"Organization image removed successfully\"}";
        return ResponseEntity.ok().body(message);
    }


    @GetMapping("/{organizationId}")
    @PreAuthorize("hasAuthority('GET_DEPARTMENTS_OF_ORGANIZATION')")
    public ResponseEntity<Page<DepartmentsResponseDTO>> getDepartmentsOfOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws EntityNotFoundException {

        Page<Department> departmentPage = organizationService.getDepartmentsOfOrganization(organizationId, page, size);

        Page<DepartmentsResponseDTO> departmentsResponseDTOPage = departmentPage.map(department -> {
            DepartmentsResponseDTO departmentResponseDTO = new DepartmentsResponseDTO();
            departmentResponseDTO.setDepartmentId(department.getDepartmentId());
            departmentResponseDTO.setDepartment(department.getDepartment());
            departmentResponseDTO.setDescription(department.getDescription());
            departmentResponseDTO.setManagerId(department.getManager().getEmployeeId());
            departmentResponseDTO.setAttributes(department.getAttributes());
            departmentResponseDTO.setOrganizations(department.getOrganizations());
            departmentResponseDTO.setBranches(department.getBranches());
            departmentResponseDTO.setManagerFirstName(department.getManager().getFirstName());
            departmentResponseDTO.setManagerEmployeeCode(department.getManager().getEmployeeCode());
            departmentResponseDTO.setManagerLastName(department.getManager().getLastName());

            return departmentResponseDTO;
        });

        return ResponseEntity.ok(departmentsResponseDTOPage);
    }


    @GetMapping("/branches/{organizationId}")
    @PreAuthorize("hasAuthority('GET_BRANCHES_OF_ORGANIZATION')")
    public ResponseEntity<Map<String, Object>> getBranchesOfOrganization(@PathVariable("organizationId") Long organizationId)throws EntityNotFoundException{
        List<Branch> branchList = organizationService.getBranchesOfOrganization(organizationId);

        List<BranchResponseDTO> branchResponseDTOList = branchList.stream().map(branch -> {
            BranchResponseDTO branchResponseDTO = new BranchResponseDTO();
            branchResponseDTO.setBranchId(branch.getBranchId());
            branchResponseDTO.setOrganizations(branch.getOrganizations());
            branchResponseDTO.setBranchName(branch.getBranchName());
            branchResponseDTO.setAttributes(branch.getAttributes());
            branchResponseDTO.setDepartments(branch.getDepartments());
            return branchResponseDTO;
        }).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Branches fetched successfully", "BranchList", branchResponseDTOList));
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_ORGANIZATIONS')")
    @GetMapping("")
    public ResponseEntity<Page<Organization>> getOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Organization> organizations = organizationService.getOrganizations(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(organizations);
    }

//    @PreAuthorize("hasAnyAuthority('CREATE_ORGANIZATION')")
//    @PostMapping("")
//    public ResponseEntity<Object> saveOrganization(@Valid @RequestBody AddNewOrganizationDTO organizationDTO) throws OrganizationService.OrganizationAlreadyExistsException, IllegalArgumentException {
//        Organization organizationAdded = organizationService.addOrganization(organizationDTO);
//        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Organization is created successfully",organizationAdded);
//    }

    @PreAuthorize("hasAnyAuthority('CREATE_ORGANIZATION')")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> saveOrganization(
            @RequestParam("organizationData") String organizationData,
            @RequestParam(value = "file", required = false) MultipartFile file)
            throws OrganizationService.OrganizationAlreadyExistsException,
            IllegalArgumentException,
            ValidationException,
            MaxUploadSizeExceededException,
            IOException {

        // Convert the organizationData (JSON) to AddNewOrganizationDTO
        ObjectMapper objectMapper = new ObjectMapper();
        AddNewOrganizationDTO organizationDTO = objectMapper.readValue(organizationData, AddNewOrganizationDTO.class);

        // Call the service method to handle both organization creation and image upload transactionally
        Organization organizationAdded = organizationService.addOrganizationWithImage(organizationDTO, file);

        return ResponseGenerator.generateResponse(HttpStatus.CREATED,true,"Organization is created successfully",organizationAdded);
    }

    @PreAuthorize("hasAnyAuthority('UPDATE_ORGANIZATION')")
    @PatchMapping(value = "/{organizationId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> updateOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam("organizationData") String organizationData,
            @RequestParam(value = "file", required = false) MultipartFile file)
            throws EntityNotFoundException, ValidationException, MaxUploadSizeExceededException, IOException {

        // Convert the organizationData (JSON) to UpdateOrganizationDTO
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateOrganizationDTO organizationDTO = objectMapper.readValue(organizationData, UpdateOrganizationDTO.class);

        // Update the organization
        Organization organizationUpdated = organizationService.updateOrganization(organizationDTO, organizationId);

        // Handle the file upload if a file is provided
        if (file != null && !file.isEmpty()) {
            // Validate and process the image
            organizationService.validateImage(file);

            // Update the organization's image (You can modify this to suit your use case, e.g., saving to a file system or database)
            organizationService.uploadOrganizationImage(organizationUpdated.getOrganizationId(), file);
        }
        else{
            organizationService.removeOrganizationImage(organizationUpdated.getOrganizationId());
        }


        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "Organization updated successfully", organizationUpdated);
    }


    @PreAuthorize("hasAnyAuthority('DELETE_ORGANIZATION')")
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Object> deleteOrganization(@PathVariable Long organizationId) throws EntityNotFoundException
    {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Organization deleted successfully."));
    }

    @ExceptionHandler({
            OrganizationService.OrganizationAlreadyExistsException.class,
            IllegalArgumentException.class,
            IllegalStateException.class,
            EntityNotFoundException.class,
            ValidationException.class,
            MaxUploadSizeExceededException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof OrganizationService.OrganizationAlreadyExistsException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof MaxUploadSizeExceededException) {
            message = "File size should be less than 5Mb";
            status = HttpStatus.PAYLOAD_TOO_LARGE;
        }
        else if (exception instanceof ValidationException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof IllegalStateException) {
        message = exception.getMessage();
        status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof EntityNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }
        else if (exception instanceof IllegalArgumentException) {
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

