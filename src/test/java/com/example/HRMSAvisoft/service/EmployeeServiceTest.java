package com.example.HRMSAvisoft.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMSAvisoft.config.CloudinaryConfiguration;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private EmployeeService employeeService;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;


    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

                    CloudinaryConfiguration cloudinaryConfiguration = Mockito.mock(CloudinaryConfiguration.class);
            Mockito.when(cloudinaryConfiguration.cloudinary())
                    .thenReturn(new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret)));

            Map<?, ?> params = ObjectUtils.asMap(
                    "public_id", "profile_images/" + 1L, // You can change the public_id format as you need
                    "folder", "profile_images" // Optional: folder in Cloudinary to organize your images
            );
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "image.jpg",
                    "image/jpeg",
                    "file content".getBytes()
            );

            Mockito.when(cloudinaryConfiguration.cloudinary().uploader().upload(file.getBytes(), params)).thenReturn(new HashMap());
    }
//    @Test
//    @DisplayName("Test Get All Employees")
//    void testGetAllEmployees() {
//        // Given
//        Employee employee1 = new Employee();
//        employee1.setEmployeeId(1L);
//        employee1.setFirstName("Test1");
//        employee1.setLastName("User");
//
//        Employee employee2 = new Employee();
//        employee2.setEmployeeId(2L);
//        employee2.setFirstName("Test2");
//        employee2.setLastName("User");
//
//
//        List<Employee> employees = Arrays.asList(employee1, employee2);
//
//        Mockito.when(employeeRepository.findAll()).thenReturn(employees);
//
//        // When
//        List<Employee> retrievedEmployees = employeeService.getAllEmployees();
//
//        // Then
//        Assertions.assertEquals(2, retrievedEmployees.size());
//        Assertions.assertEquals("Test1", retrievedEmployees.get(0).getFirstName());
//        Assertions.assertEquals("Test2", retrievedEmployees.get(1).getFirstName());
//        Mockito.verify(employeeRepository, Mockito.times(1)).findAll();
//    }

    @Test
    @DisplayName("Test Update Employee")
    public void testUpdateEmployee() {
        // Prepare mock data
        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(1L);
        existingEmployee.setFirstName("John");
        existingEmployee.setLastName("Doe");
        // Update information
        existingEmployee.setFirstName("Updated First Name");
        existingEmployee.setLastName("Updated Last Name");

        // Mock repository behavior
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        // Call the service method
        Employee updatedEmployee = employeeService.updateEmployee(existingEmployee);

        // Assert the result
        assertEquals("Updated First Name", updatedEmployee.getFirstName());
        assertEquals("Updated Last Name", updatedEmployee.getLastName());
    }

    @Test
    @DisplayName("Test Get Employee By ID")
    void testGetEmployeeById() throws Exception{
        // Given
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John Doe");

        Mockito.when(employeeRepository.getByEmployeeId(employeeId)).thenReturn(employee);

        // When
        Employee retrievedEmployee = employeeService.getEmployeeById(employeeId);

        // Then
        Assertions.assertEquals(employeeId, retrievedEmployee.getEmployeeId());
        Assertions.assertEquals("John Doe", retrievedEmployee.getFirstName());
        Mockito.verify(employeeRepository, Mockito.times(1)).getByEmployeeId(employeeId);
    }

    //        @Test
//        @DisplayName("test_validEmployeeIdAndValidImageFile")
//        public void test_validEmployeeIdAndValidImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
//            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//
//
//            Employee employee = new Employee();
//            employee.setEmployeeId(1L);
//
//            CloudinaryConfiguration cloudinaryConfiguration = Mockito.mock(CloudinaryConfiguration.class);
//
//            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//            MockMultipartFile file = new MockMultipartFile(
//                    "file",
//                    "image.jpg",
//                    "image/jpeg",
//                    "file content".getBytes()
//            );
//
//
//            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinaryConfiguration.cloudinary());
//
//            employeeService.uploadProfileImage(1L, file);
//
//            assertEquals("image.jpg", employee.getProfileImage());
//
//            Mockito.verify(employeeRepository).save(employee);
//        }


//        @Test
//        @DisplayName("test_invalidEmployeeIdAndValidImageFile")
//        public void test_invalidEmployeeIdAndValidImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
//            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);
//            Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
//
//            MultipartFile file = Mockito.mock(MultipartFile.class);
//
//            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);
//
//            assertThrows(EmployeeService.EmployeeNotFoundException.class, () -> {
//                employeeService.uploadProfileImage(2L, file);
//            });
//        }
//
//        @Test
//        @DisplayName("test_validEmployeeIdAndNullImageFile")
//        public void test_validEmployeeIdAndNullImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
//            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//
//            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);
//
//            Employee employee = new Employee();
//            employee.setEmployeeId(1L);
//
//            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);
//
//            assertThrows(NullPointerException.class, () -> {
//                employeeService.uploadProfileImage(1L, null);
//            });
//        }
//
//        @Test
//        @DisplayName("test_validEmployeeIdAndEmptyImageFile")
//        public void test_validEmployeeIdAndEmptyImageFile() throws EmployeeService.EmployeeNotFoundException, IOException, NullPointerException {
//            EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
//
//            Cloudinary cloudinary = Mockito.mock(Cloudinary.class);
//
//            Employee employee = new Employee();
//            employee.setEmployeeId(1L);
//
//            Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//            MultipartFile file = Mockito.mock(MultipartFile.class);
//
//            byte[] fileContent = new byte[0];
//            Mockito.when(file.getBytes()).thenReturn(fileContent);
//
//            EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);
//
//            assertThrows(NullPointerException.class, () -> {
//                employeeService.uploadProfileImage(1L, file);
//            });
//        }

    @Test
    @DisplayName("test_return_matching_employees")
    void test_returns_matching_employees() {

        String name = "John";
        List<Employee> expectedEmployees = new ArrayList<>();
        Employee employee1 = new Employee();
        employee1.setFirstName("John");
        expectedEmployees.add(employee1);
        Employee employee2 = new Employee();
        employee2.setFirstName("John");
        expectedEmployees.add(employee2);

        when(employeeRepository.searchEmployeesByName(name)).thenReturn(expectedEmployees);
        List<Employee> searchedEmployees = employeeService.searchEmployeesByName(name);

        assertEquals(expectedEmployees, searchedEmployees);
    }


    @Test
    @DisplayName("test_invalidEmployeeIdAndValidImageFile")
    public void test_invalidEmployeeIdAndValidImageFile() throws EmployeeNotFoundException, IOException, NullPointerException {
        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
        Cloudinary cloudinary = Mockito.mock(Cloudinary.class);
        Mockito.when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        MultipartFile file = Mockito.mock(MultipartFile.class);


        EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.uploadProfileImage(2L, file);
        });
    }

    @Test
    @DisplayName("test_validEmployeeIdAndNullImageFile")
    public void test_validEmployeeIdAndNullImageFile() throws EmployeeNotFoundException, IOException, NullPointerException {
        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

        Cloudinary cloudinary = Mockito.mock(Cloudinary.class);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeService employeeService = new EmployeeService(employeeRepository,cloudinary);

        assertThrows(NullPointerException.class, () -> {
            employeeService.uploadProfileImage(1L, null);
        });
    }

    @Test
    @DisplayName("test_validEmployeeIdAndEmptyImageFile")
    public void test_validEmployeeIdAndEmptyImageFile() throws EmployeeNotFoundException, IOException, NullPointerException {
        EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);

        Cloudinary cloudinary = Mockito.mock(Cloudinary.class);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        MultipartFile file = Mockito.mock(MultipartFile.class);

        byte[] fileContent = new byte[0];
        Mockito.when(file.getBytes()).thenReturn(fileContent);

        EmployeeService employeeService = new EmployeeService(employeeRepository, cloudinary);

        assertThrows(NullPointerException.class, () -> {
            employeeService.uploadProfileImage(1L, file);
        });
    }


}
