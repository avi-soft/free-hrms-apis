package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AccountRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private EmployeeRepository employeeRepository;
    public AccountService(AccountRepository accountRepository,EmployeeRepository employeeRepository){
        this.accountRepository=accountRepository;
        this.employeeRepository=employeeRepository;
    }
    public Employee addAccountToEmployee(Long employeeId, AddAccountDTO accountDTO) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));
        Account existingAccount = employee.getAccount();
        if (existingAccount != null) {
            accountRepository.deleteById(existingAccount.getAccountId());
        }
        Account account=new Account();
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setIfsc(accountDTO.getIfsc());
        account.setBankName(accountDTO.getBankName());
        account.setBranch(accountDTO.getBranch());
        Account newAccount =accountRepository.save(account);
        employee.setAccount(newAccount);
        return employeeRepository.save(employee);

    }
    public boolean removeAccountFromEmployee(Long employeeId)throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException( employeeId));
        if (employee.getAccount() != null) {
            employee.setAccount(null);
            employeeRepository.save(employee);
            return true;
        }
        return false;
    }



}
