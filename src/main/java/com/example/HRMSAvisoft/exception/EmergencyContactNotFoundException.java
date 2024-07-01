package com.example.HRMSAvisoft.exception;

public class EmergencyContactNotFoundException extends Exception{
    public EmergencyContactNotFoundException(Long contactId){
        super("Emergency contact not found with ID: " + contactId);
    }
    public EmergencyContactNotFoundException(Long contactId,Long employeeId){
        super("Employee with ID :"+employeeId+" does not contain Emergency Contact with ID:"+contactId);
    }
}
