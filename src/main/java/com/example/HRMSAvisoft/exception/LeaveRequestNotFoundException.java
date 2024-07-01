package com.example.HRMSAvisoft.exception;

public class LeaveRequestNotFoundException extends Exception {
    public LeaveRequestNotFoundException(String leaveRequestNotFound) {
        super(leaveRequestNotFound);
    }
}
