package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utils.ResponseGenerator;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final ModelMapper modelMapper;

    private final RoleService roleService;

    RoleController(ModelMapper modelMapper, RoleService roleService){
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    @PreAuthorize("hasAnyAuthority('GET_ALL_ROLES')")
    @GetMapping("")
    public ResponseEntity<List<Role>> getRoles(){
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_ROLE')")
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> saveRole(@RequestBody Role role) throws RoleService.RoleAlreadyExistsException, IllegalArgumentException {
        Role roleAdded = roleService.addRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("success",true, "message", "Role created successfully.", "role", roleAdded));
    }

//    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE')")
    @PatchMapping("/{roleId}")
    public ResponseEntity<Object> updateRole(@RequestBody Role role, @PathVariable Long roleId) throws EntityNotFoundException, IllegalArgumentException{
        Role updatedRole = roleService.updateRole(role, roleId);
        return ResponseGenerator.generateResponse(HttpStatus.OK, true, "Role Updated successfully.",updatedRole);
    }

    @PreAuthorize("hasAnyAuthority('DELETE_ROLE')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Object> deleteRole(@PathVariable Long roleId) throws EntityNotFoundException
    {
        Role deletedRole=roleService.deleteRole(roleId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"Role is Deleted successfully",deletedRole);
    }

    @PreAuthorize("hasAnyAuthority('CHANGE_USER_ROLE')")
    @PatchMapping("/changeRole/{userId}/{oldRoleId}/{newRoleId}")
    public ResponseEntity<Object> changeRoleOfUser(@PathVariable Long userId, @PathVariable Long oldRoleId,@PathVariable Long newRoleId)
    {
        Role changedRole = roleService.changeRoleOfUser(userId,oldRoleId,newRoleId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"Role of User is changed with new Role",changedRole);
    }
    @PreAuthorize("hasAnyAuthority('ASSIGN_ROLE_TO_USER')")
    @PatchMapping("/{userId}/assignRole/{roleId}")
    public ResponseEntity<Object> assignRoleToExistingUser(@PathVariable Long userId, @PathVariable Long roleId)
    {
        Role changedRole = roleService.assignRoleToExistingUser(userId,roleId);
        return ResponseGenerator.generateResponse(HttpStatus.OK,true,"Role is assigned to User successfully",changedRole);
    }

    @ExceptionHandler({
            RoleService.RoleAlreadyExistsException.class,
            IllegalArgumentException.class
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception) {
        String message;
        HttpStatus status;
        if (exception instanceof RoleService.RoleAlreadyExistsException) {
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
