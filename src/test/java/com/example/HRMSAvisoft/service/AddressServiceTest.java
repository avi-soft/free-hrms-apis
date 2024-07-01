package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.AddressType;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Zipcode;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.ZipCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    AddressRepository addressRepository;
    @Mock
    ZipCodeRepository zipCodeRepository;
    @InjectMocks
    AddressService addressService;


    @Test
    void addAddressToEmployee_Success() throws EmployeeNotFoundException {
        // Mock data
        Long employeeId = 1L;
        AddressDTO addressDTO = new AddressDTO("123", AddressType.PERMANENT, 123456L, "City", "State", "Country");


        // Create employee
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Address address= new Address();
        employee.setAddresses(new ArrayList<>());

        // Create Zipcode
        Zipcode zipcode = new Zipcode();
        zipcode.setZipCode(12345L);
        zipcode.setCity("City");
        zipcode.setState("State");

        when(zipCodeRepository.save(any(Zipcode.class))).thenReturn(zipcode);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address arg = invocation.getArgument(0);
            arg.setAddressId(1L); // Set a mock ID
            return arg;
        });
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        // Test method
        Employee result = addressService.addAddressToEmployee(employeeId, addressDTO);


        // Assertions
        assertNotNull(result);
        assertEquals(1, result.getAddresses().size());
        Address addedAddress = result.getAddresses().iterator().next();
        assertEquals(addressDTO.getPropertyNumber(), addedAddress.getPropertyNumber());
        assertEquals(addressDTO.getCountry(), addedAddress.getCountry());
        assertEquals(zipcode, addedAddress.getZipCode());
        assertEquals(addressDTO.getAddressType(), addedAddress.getAddressType());
        // Verify interactions
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(zipCodeRepository, times(1)).save(any(Zipcode.class));
        verify(addressRepository, times(1)).save(any(Address.class));

    }

    @Test
    void addAddressToEmployee_EmployeeNotFound(){
        Long employeeId = 1L;
        AddressDTO addressDTO = new AddressDTO("123", AddressType.PERMANENT, 123456L, "City", "State", "Country");

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Test method and assertions
        assertThrows(EmployeeNotFoundException.class,
                () -> addressService.addAddressToEmployee(employeeId, addressDTO));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(zipCodeRepository, never()).save(any());
        verify(addressRepository, never()).save(any());
        verify(employeeRepository, never()).save(any());

    }
    @Test
    void editAddress_Success()throws EmployeeNotFoundException {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO("123", AddressType.TEMPORARY, 12345L, "City", "State", "Country");
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Address address = new Address();
        address.setAddressId(addressId);
        address.setPropertyNumber("Old Property Number");
        address.setAddressType(AddressType.TEMPORARY);
        Zipcode zipcode = new Zipcode();
        zipcode.setCity("Old City");
        zipcode.setState("Old State");
        zipcode.setZipCode(12345L);
        address.setZipCode(zipcode);
        employee.getAddresses().add(address);

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(zipCodeRepository.save(any(Zipcode.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method under test
        Employee updatedEmployee = addressService.editAddress(employeeId, addressId, addressDTO);

        // Verify interactions and assertions
        assertEquals(employee, updatedEmployee);
        assertEquals(1, updatedEmployee.getAddresses().size());
        Address updatedAddress = updatedEmployee.getAddresses().get(0);
        assertEquals(addressDTO.getPropertyNumber(), updatedAddress.getPropertyNumber());
        assertEquals(addressDTO.getAddressType(), updatedAddress.getAddressType());
        assertEquals(addressDTO.getCity(), updatedAddress.getZipCode().getCity());
        assertEquals(addressDTO.getState(), updatedAddress.getZipCode().getState());
        assertEquals(addressDTO.getZipCode(), updatedAddress.getZipCode().getZipCode());
        assertEquals(addressDTO.getCountry(), updatedAddress.getCountry());
        verify(employeeRepository).findById(employeeId);
        verify(addressRepository).findById(addressId);
        verify(zipCodeRepository).save(any(Zipcode.class));
        verify(addressRepository).save(any(Address.class));
    }
    @Test
    void editAddress_EmployeeNotFound() {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Call the method under test and assert exception
        assertThrows(EmployeeNotFoundException.class,
                () -> addressService.editAddress(employeeId, addressId, new AddressDTO()));
        verify(employeeRepository).findById(employeeId);
        verify(addressRepository, never()).findById(any());
        verify(zipCodeRepository, never()).save(any());
        verify(addressRepository, never()).save(any());
    }
    @Test
    void editAddress_AddressNotFound() {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;
        AddressDTO addressDTO = new AddressDTO();

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // Call the method under test and assert exception
        assertThrows(AddressService.AddressNotFoundException.class,
                () -> addressService.editAddress(employeeId, addressId, addressDTO));
        verify(employeeRepository).findById(employeeId);
        verify(addressRepository).findById(addressId);
        verify(zipCodeRepository, never()).save(any());
        verify(addressRepository, never()).save(any());
    }
    @Test
    void removeAddressFromEmployee_EmployeeNotFound() {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Test method and assertions
        assertThrows(EmployeeNotFoundException.class,
                () -> addressService.removeAddressFromEmployee(employeeId, addressId));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(addressRepository, never()).findById(any());
        verify(employeeRepository, never()).save(any());
    }
    @Test
    void removeAddressFromEmployee_Success() throws EmployeeNotFoundException, AddressService.AddressNotFoundException {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;

        // Create employee
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Address addressToRemove = new Address();
        addressToRemove.setAddressId(addressId);

        // Add address to employee
       List<Address> addresses=new ArrayList<>();
        addresses.add(addressToRemove);
        employee.setAddresses(addresses);

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressToRemove));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        // Test method
        Employee result = addressService.removeAddressFromEmployee(employeeId, addressId);

        // Verify interactions
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(employeeRepository, times(1)).save(employee);


        // Assertions
        assertNotNull(result);
        assertFalse(result.getAddresses().contains(addressToRemove));
    }
    @Test
    void removeAddressFromEmployee_AddressNotFound() {
        // Mock data
        Long employeeId = 1L;
        Long addressId = 1L;

        // Create employee
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // Test method and assertions
        assertThrows(AddressService.AddressNotFoundException.class,
                () -> addressService.removeAddressFromEmployee(employeeId, addressId));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(addressRepository, times(1)).findById(addressId);
        verify(employeeRepository, never()).save(any());
    }

}
