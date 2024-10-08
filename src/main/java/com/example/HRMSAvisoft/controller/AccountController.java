package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.AddAccountDTO;
import com.example.HRMSAvisoft.entity.Account;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.AccountService;
import com.example.HRMSAvisoft.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }

    @PreAuthorize("hasAuthority('ADD_ACCOUNT')")
    @PostMapping("/{employeeId}/AddBankAccount")
    public ResponseEntity<Map<String,Object>>addAccount(@RequestBody @Valid AddAccountDTO accountDTO, @PathVariable Long employeeId)throws EmployeeNotFoundException {
        Employee updatedEmployee=accountService.addAccountToEmployee(employeeId,accountDTO);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("message", "Account Added");
        response.put("success", true);
        response.put("updatedUser", updatedEmployee);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{accountId}")
    public ResponseEntity<Map<String, Object>> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AddAccountDTO addAccountDTO){
        Account updatedAccount = accountService.updateAccount(accountId,addAccountDTO);

        return ResponseEntity.status(200).body(Map.of("success", true, "message", "Account updated successfully", "updatedAccount", updatedAccount));
    }

    @PreAuthorize("hasAuthority('REMOVE_ACCOUNT')")
    @DeleteMapping("/{employeeId}/removeAccount")
    public ResponseEntity<Map<String,Object>> removeAccountFromEmployee(@PathVariable Long employeeId)throws EmployeeNotFoundException {
        Map<String, Object> response = new HashMap<String, Object>();
        boolean removed = accountService.removeAccountFromEmployee(employeeId);
        if (removed) {
            response.put("message", "Account Removed");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Account Field is empty");
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }
}


