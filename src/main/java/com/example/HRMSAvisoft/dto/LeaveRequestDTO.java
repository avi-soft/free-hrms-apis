package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {
    private Long id;
    private Long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
    private String leaveType;
    private LocalDate startDate ;
    private LocalDate endDate;
    private Integer numberOfDays;
    private String reason;
    private AddressType addressType;
}
