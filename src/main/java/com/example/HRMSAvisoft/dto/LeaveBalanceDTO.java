package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.LeaveType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class LeaveBalanceDTO {

    private String leaveType;
    private int accruedLeave;
    private int usedLeave;
    private int carryForward;
}
