package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.repository.AccountRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public Account updateAccount(Long accountId, AddAccountDTO addAccountDTO){
        Account accountToUpdate = accountRepository.findById(accountId).orElseThrow(()-> new EntityNotFoundException("Account not found."));

        if(Objects.nonNull(addAccountDTO.getAccountNumber()) && !addAccountDTO.getAccountNumber().equals("")){
            accountToUpdate.setAccountNumber(addAccountDTO.getAccountNumber());
        }
        if(Objects.nonNull(addAccountDTO.getIfsc()) && !addAccountDTO.getIfsc().equals("")){
            accountToUpdate.setIfsc(addAccountDTO.getIfsc());
        }
        if(Objects.nonNull(addAccountDTO.getBranch()) && !addAccountDTO.getBranch().equals("")){
            accountToUpdate.setBranch(addAccountDTO.getBranch());
        }
        if(Objects.nonNull(addAccountDTO.getBankName()) && !addAccountDTO.getBankName().equals("")){
            accountToUpdate.setBankName(addAccountDTO.getBankName());
        }

        return accountRepository.save(accountToUpdate);
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
