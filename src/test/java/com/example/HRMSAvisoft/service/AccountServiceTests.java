package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AccountRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("AddAccountToEmployee_Success")
    void addAccountToEmployee_Success() throws EmployeeNotFoundException {
        Long employeeId = 1L;
        AddAccountDTO accountDTO = new AddAccountDTO("1234567890", "IFSC1234", "BankName", "Branch");
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("demo");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setIfsc(accountDTO.getIfsc());
        account.setBankName(accountDTO.getBankName());
        account.setBranch(accountDTO.getBranch());
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        employee.setAccount(account);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee updatedEmployee = accountService.addAccountToEmployee(employeeId, accountDTO);

        assertNotNull(updatedEmployee);
        assertEquals(account, updatedEmployee.getAccount());
    }

    @Test
    @DisplayName("addAccountToEmployee_EmployeeNotFound")
    void addAccountToEmployee_EmployeeNotFound() {
        // Mock data
        Long employeeId = 1L;
        AddAccountDTO accountDTO = new AddAccountDTO();

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Call the method under test and assert exception
        assertThrows(EmployeeNotFoundException.class,
                () -> accountService.addAccountToEmployee(employeeId, accountDTO));
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(accountRepository, never()).save(any());
        verify(employeeRepository, never()).save(any());
    }


    @Test
    @DisplayName("removeAccountFromEmployee_success")
void removeAccountFromEmployee_Success() throws EmployeeNotFoundException{
    Long employeeId = 1L;
    Employee employee = new Employee();
    employee.setEmployeeId(employeeId);
    Account account = new Account();
    employee.setAccount(account);

    when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

    assertTrue(accountService.removeAccountFromEmployee(employeeId));
    assertNull(employee.getAccount());
    verify(employeeRepository, times(1)).save(employee);
}

    @Test
    @DisplayName("removeAccountFromEmployee_accountDoesNotExist")
    void removeAccountFromEmployee_AccountNotExists() throws EmployeeNotFoundException{
        // Mock data
        Long employeeId = 1L;
        Employee employee = new Employee();

        // Stub repository methods
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Call the method under test
        boolean result = accountService.removeAccountFromEmployee(employeeId);

        // Verify interactions
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any());

        // Assertions
        assertFalse(result);
        assertNull(employee.getAccount());
    }

}
