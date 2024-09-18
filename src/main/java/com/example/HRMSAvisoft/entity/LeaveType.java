package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long leaveTypeId;

@Column(nullable = false, unique = true)
private String name;

private String description;

@Column(nullable = false)
private Integer allowedDaysPerYear;

@Column(nullable = false)
private Boolean isPaidLeave;

//@ManyToOne
//@JoinColumn(name = "organization_id", nullable = false)
//private Organization organization;
}
