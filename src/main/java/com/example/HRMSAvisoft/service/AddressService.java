package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Zipcode;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.ZipCodeRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private AddressRepository addressRepository;
    private ZipCodeRepository zipcodeRepository;
    private EmployeeRepository employeeRepository;

    public AddressService(AddressRepository addressRepository,EmployeeRepository employeeRepository,ZipCodeRepository
                        zipcodeRepository){
        this.addressRepository=addressRepository;
        this.employeeRepository=employeeRepository;
        this.zipcodeRepository=zipcodeRepository;
    }
    public Employee addAddressToEmployee(Long employeeId, AddressDTO address) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));

        Address addAddress=new Address();
        addAddress.setPropertyNumber(address.getPropertyNumber());
        addAddress.setCountry(address.getCountry());

        Zipcode zipcode =new Zipcode();
        zipcode.setCity(address.getCity());
        zipcode.setState(address.getState());
        zipcode.setZipCode(address.getZipCode());
        zipcode= zipcodeRepository.save(zipcode);
        addAddress.setZipCode(zipcode);
        addAddress.setAddressType(address.getAddressType());

        Address newAddress =addressRepository.save(addAddress);
        employee.getAddresses().add(newAddress);

        return employeeRepository.save(employee);
    }
    public Employee removeAddressFromEmployee(Long employeeId, Long addressId) throws EmployeeNotFoundException {
        // Retrieve the employee entity by its ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));

        Address addressToRemove=addressRepository.findById(addressId)
                .orElseThrow(()->new AddressService.AddressNotFoundException(addressId));

        if (addressToRemove != null) {
            if (employee.getAddresses().contains(addressToRemove)) {
                employee.getAddresses().remove(addressToRemove);
            } else {
                throw new AddressService.AddressNotFoundException(employeeId,addressId);
            }
        } else {
            throw new AddressService.AddressNotFoundException(addressId);
        }

        return employeeRepository.save(employee);
    }

    public Employee editAddress(Long employeeId,Long addressId,AddressDTO addressDTO)throws EmployeeNotFoundException,AddressNotFoundException{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));
        Address addressToEdit = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        if (!employee.getAddresses().contains(addressToEdit)) {
            throw new AddressNotFoundException(employeeId, addressId);
        }
        addressToEdit.setPropertyNumber(addressDTO.getPropertyNumber());
        addressToEdit.setAddressType(addressDTO.getAddressType());

        Zipcode editedZipcode =new Zipcode();
        editedZipcode.setCity(addressDTO.getCity());
        editedZipcode.setState(addressDTO.getState());
        editedZipcode.setZipCode(addressDTO.getZipCode());
        editedZipcode= zipcodeRepository.save(editedZipcode);

        addressToEdit.setZipCode(editedZipcode);
        addressToEdit.setCountry(addressDTO.getCountry());

        addressRepository.save(addressToEdit);

        return employee;
    }

    public static class AddressNotFoundException extends RuntimeException{
        public AddressNotFoundException(Long addressId){super("Address not found with ID: " + addressId);}
        public AddressNotFoundException(Long employeeId,Long addressId){super("Employee with ID :"+employeeId+" does not contain address with ID : "+addressId);}
    }
}
