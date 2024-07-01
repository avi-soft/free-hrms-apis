package com.example.HRMSAvisoft.exception;

public class OverlappingLeaveRequestException extends Exception {
    public OverlappingLeaveRequestException( ) {
        super("Leave request overlaps with existing leave requests.");
    }
}
