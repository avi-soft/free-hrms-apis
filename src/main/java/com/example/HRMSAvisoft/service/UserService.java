package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddNewUserDTO;
import com.example.HRMSAvisoft.dto.CreateUserDTO;
import com.example.HRMSAvisoft.dto.LoginUserDTO;
import com.example.HRMSAvisoft.dto.UserInfoDTO;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Organization;
import com.example.HRMSAvisoft.entity.Role;
import com.example.HRMSAvisoft.entity.User;
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
@Transactional
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

    public Employee saveUser(CreateUserDTO createUserDTO, User loggedInUser, Long organizationId) throws IOException {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(()-> new EntityNotFoundException("Organization not found"));
        User alreadyRegisteredUser = userRepository.getByEmail(createUserDTO.getEmail());

        if(alreadyRegisteredUser!=null){
            throw new EmailAlreadyExistsException(createUserDTO.getEmail());
        }

        User newUser=new User();
        newUser.setEmail(createUserDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        newUser.setCreatedBy(loggedInUser);
        LocalDateTime createdAt = LocalDateTime.now();
        DateTimeFormatter createdAtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        newUser.setCreatedAt(createdAt.format(createdAtFormatter));

        Role roleToAdd = roleRepository.getByRole(createUserDTO.getRole()).orElse(null);
        if(roleToAdd != null) {
            newUser.getRoles().clear();
            newUser.getRoles().add(roleToAdd);
        }

        // make employee instance corresponding to the user and set some data of employee

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(createUserDTO.getFirstName());
        newEmployee.setLastName(createUserDTO.getLastName());
//        newEmployee.setJoinDate(createUserDTO.getJoinDate());
//        newEmployee.setGender(createUserDTO.getGender());
//        newEmployee.setPosition(createUserDTO.getPosition());
//        newEmployee.setSalary(createUserDTO.getSalary());
//        newEmployee.setDateOfBirth(createUserDTO.getDateOfBirth());
        newEmployee.setProfileImage("https://api.dicebear.com/5.x/initials/svg?seed="+createUserDTO.getFirstName()+" "+createUserDTO.getLastName());
        Employee savedEmployee = employeeRepository.save(newEmployee);

        newUser.setEmployee(savedEmployee);
        organizationRepository.save(organization);
        userRepository.save(newUser);
        return savedEmployee;

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

    public boolean deleteUser(Long userId)throws EmployeeNotFoundException {


        User userToDelete = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));
        userRepository.delete(userToDelete);

        return true;
    }

    public Collection<Role> getUserRoles(Long userId) {
        User user = getUserById(userId);
        return user.getRoles();
    }

    private void enableForeignKeyChecks() {
        Query query = entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1");
        query.executeUpdate();
    }

    private void disableForeignKeyChecks() {
        Query query = entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0");
        query.executeUpdate();
    }

    public Page<UserInfoDTO> getAllUserInfo(int page, int size, String sortBy)  {
        List<Employee> employeesList = employeeRepository.findAll();

        // Sort the list based on the sortBy parameter
        if ("createdAt".equalsIgnoreCase(sortBy)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Sorting by parsing the string dates to LocalDateTime and handling nulls
            employeesList.sort((e1, e2) -> {
                LocalDateTime date1 = null;
                LocalDateTime date2 = null;
                try {
                    if (e1.getCreatedAt() != null && !e1.getCreatedAt().trim().isEmpty()) {
                        date1 = LocalDateTime.parse(e1.getCreatedAt(), formatter);
                    }
                    if (e2.getCreatedAt() != null && !e2.getCreatedAt().trim().isEmpty()) {
                        date2 = LocalDateTime.parse(e2.getCreatedAt(), formatter);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date: " + e.getMessage());
                }
                // Reverse order of comparison for descending order sorting
                return Comparator.nullsLast(LocalDateTime::compareTo).reversed().compare(date1, date2);
            });
        } else if ("name".equalsIgnoreCase(sortBy)) {
            // Sorting by firstName and lastName alphabetically and handling nulls
            employeesList.sort(Comparator.comparing(Employee::getFirstName, Comparator.nullsLast(String::compareTo))
                    .thenComparing(Employee::getLastName, Comparator.nullsLast(String::compareTo)));
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