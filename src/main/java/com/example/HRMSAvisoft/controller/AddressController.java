package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    private AddressService addressService;
    public AddressController(AddressService addressService)
    {
        this.addressService=addressService;
    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PostMapping("/{employeeId}/addNewAddress")
    public ResponseEntity<Map<String,Object>> addAddressToEmployee(@PathVariable Long employeeId, @RequestBody  @Valid AddressDTO address)throws EmployeeNotFoundException {
        Employee updatedEmployee = addressService.addAddressToEmployee(employeeId, address);
        return ResponseEntity.ok().body(Map.of("UpdatedEmployee",updatedEmployee ,"message", "New Address Added", "Status", true));
    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @DeleteMapping("/{employeeId}/removeAddress/{addressId}")
    public ResponseEntity<Map<String,Object>> removeAddressFromEmployee(@PathVariable Long employeeId, @PathVariable Long addressId) throws EmployeeNotFoundException{
        Employee updatedEmployee = addressService.removeAddressFromEmployee(employeeId, addressId);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee,"message", "Address Removed from Employee", "Status", true));
    }
    @PreAuthorize("hasAnyAuthority('Role_Superadmin','Role_Admin')")
    @PutMapping("/{employeeId}/editAddress/{addressId}")
    public ResponseEntity<Map<String,Object>>editAddress(@PathVariable Long employeeId,@PathVariable Long addressId,@RequestBody @Valid AddressDTO addressDTO)throws EmployeeNotFoundException,AddressService.AddressNotFoundException
    {
        Employee updatedEmployee = addressService.editAddress(employeeId, addressId,addressDTO);
        return ResponseEntity.ok(Map.of("UpdatedEmployee",updatedEmployee,"message", "Address edited", "Status", true));

    }


}
