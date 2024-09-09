package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddNewUserDTO;
import com.example.HRMSAvisoft.dto.CreateUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.UserInfoDTO;
import com.example.HRMSAvisoft.entity.*;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.OrganizationRepository;
import com.example.HRMSAvisoft.repository.RoleRepository;
import com.example.HRMSAvisoft.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    final private UserRepository userRepository;
    final private OrganizationRepository organizationRepository;

    final private PasswordEncoder passwordEncoder;

    final private RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    final private EmployeeRepository employeeRepository;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.employeeRepository=employeeRepository;
        this.organizationRepository=organizationRepository;
    }

    public User getUserById(Long id){
        User user=userRepository.getByUserId(id);
        return user;
    }


    public User addNewUser(AddNewUserDTO addNewUserDTO, User loggedInUser, Long organizationId)throws IOException,EmailAlreadyExistsException, EntityNotFoundException{
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));
        User alreadyRegisteredUser = userRepository.getByEmail(addNewUserDTO.getEmail());
        if(alreadyRegisteredUser!=null){
            throw new EmailAlreadyExistsException(addNewUserDTO.getEmail());
        }
        User newUser=new User();
        newUser.setEmail(addNewUserDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(addNewUserDTO.getPassword()));

        newUser.setCreatedBy(loggedInUser);
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newUser.setCreatedAt(createdAt.format(createdAtFormatter));

        Role roleToAdd = roleRepository.getByRole(addNewUserDTO.getRole()).orElse(null);
        if(roleToAdd != null) {
            newUser.getRoles().clear();
            newUser.getRoles().add(roleToAdd);
        }

        Employee employee=new Employee();
        employee.setUser(newUser);
        Employee savedEmployee = employeeRepository.save(employee);

        newUser.setEmployee(savedEmployee);
        organizationRepository.save(organization);
        return userRepository.save(newUser);
    }

    public User userLogin(LoginUserDTO loginUserDTO) throws EntityNotFoundException, WrongPasswordCredentialsException, IllegalAccessRoleException, IllegalArgumentException{

        if (loginUserDTO.getEmail() == null || loginUserDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (loginUserDTO.getPassword() == null || loginUserDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        User loggedInUser = userRepository.getByEmail(loginUserDTO.getEmail());
        if(loggedInUser == null){
            throw new EntityNotFoundException("User with email " + loginUserDTO.getEmail()+" not found");
        }
//        Role roleUserWantToLoginWith = roleRepository.getByRole(loginUserDTO.getRole());
//
//        if(!loggedInUser.getRoles().contains(roleUserWantToLoginWith)){
//            throw new IllegalAccessRoleException(loginUserDTO.getEmail(), loginUserDTO.getRole());
//        }
        else if(passwordEncoder.matches(loginUserDTO.getPassword(), loggedInUser.getPassword())){
            return loggedInUser;
        }
        else{
            throw new WrongPasswordCredentialsException(loggedInUser.getEmail());
        }
    }

    public static class IllegalAccessRoleException extends IllegalAccessException{
        public IllegalAccessRoleException(String email, String role){
            super(email +" does not have the access to "+ role +" role.");
        }
    }

    @Transactional
    public void deleteUser(Long userId)throws EmployeeNotFoundException {


        User userToDelete = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));

        // Clear the roles before deleting the user
        if (userToDelete.getRoles() != null && !userToDelete.getRoles().isEmpty()) {
            userToDelete.getRoles().clear();// This will update the join table and remove the association
        }
        Employee employee = userToDelete.getEmployee();
        if (employee != null) {
            // Clear relationships for the employee
            employee.getAddresses().clear();
            employee.getEmergencyContacts().clear();
            employee.getSkills().clear();
            for(Department department : employee.getDepartments()){
                if(department.getEmployees().contains(employee)){
                    department.getEmployees().remove(employee);
                }
            }
            employee.getDesignations().clear();
            employee.getAttributes().clear();
            employee.getPerformanceList().clear();
            employee.getReviewedPerformances().clear();


            // Delete employee entity
            employeeRepository.delete(employee);  // This will also remove any orphaned records because of `orphanRemoval = true`
        }

        userRepository.delete(userToDelete);

        // Force the database to synchronize changes
        entityManager.flush();

    }

    public Collection<Role> getUserRoles(Long userId) {
        User user = getUserById(userId);
        return user.getRoles();
    }

    public Page<UserInfoDTO> getAllUserInfo(int page, int size, String sortBy)  {
        List<Employee> employeesList = employeeRepository.findAll();

        // Sort the list based on the sortBy parameter
        if (!"noSort".equalsIgnoreCase(sortBy)) {
            if ("createdAt".equalsIgnoreCase(sortBy)) {
                // Sorting based on userId in descending order
                employeesList.sort((e1, e2) -> {
                    User user1 = userRepository.findByEmployee(e1);
                    User user2 = userRepository.findByEmployee(e2);

                    // Compare userId in reverse order for descending sorting
                    return Comparator.nullsLast(Long::compareTo).reversed().compare(user1.getUserId(), user2.getUserId());
                });
            } else if ("name".equalsIgnoreCase(sortBy)) {
                // Sorting by firstName and lastName alphabetically and handling nulls
                employeesList.sort(Comparator.comparing(Employee::getFirstName, Comparator.nullsLast(String::compareTo))
                        .thenComparing(Employee::getLastName, Comparator.nullsLast(String::compareTo)));
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        // Paginate the sorted list
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), employeesList.size());

        List<UserInfoDTO> userInfoDTOList = employeesList.subList(start, end).stream().map(employee -> {
            User user = userRepository.findByEmployee(employee);
            return new UserInfoDTO(
                    employee.getEmployeeId(),
                    user.getUserId(),
                    employee.getEmployeeCode(),
                    user.getEmail(),
                    employee.getFirstName() + " " + employee.getLastName(),
                    employee.getProfileImage()
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(userInfoDTOList, pageable, employeesList.size());
    }

    public static class WrongPasswordCredentialsException extends IllegalAccessException{
        public WrongPasswordCredentialsException(String email){
            super("Wrong password for " + email);
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException{
        public EmailAlreadyExistsException(String email){
            super(email+ " already exists");
        }
    }

    public static class UserNotFoundException extends RuntimeException{
        public UserNotFoundException(Long id){
            super("User with ID:" +id+" not found!!");
        }
    }


}