package com.example.HRMSAvisoft.exception;

public  class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(Long employeeId) {
        super("Employee not found with ID: " + employeeId);
    }
}