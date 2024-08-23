package com.example.HRMSAvisoft.entity;

import com.example.HRMSAvisoft.attribute.Attribute;
import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import utils.AttributesSerializer;
//import utils.;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@JsonFormat
@Builder
//@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long employeeId;

    @Column(unique = true)
    private String employeeCode;

    private String firstName;
//
    private String lastName;
//    private String contact;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<Address>();

    //    @Enumerated(EnumType.STRING)
    //    private Position position;
    //
    //    private String joinDate;
    //
        @Enumerated(EnumType.STRING)
        private Gender gender;
    //
    //    private String adhaarNumber;
    //
    //    private String panNumber;
    //
    //    private String uanNumber;
    //
        private String profileImage;
    //
    //    private String dateOfBirth;
    //
    //    private BigDecimal salary;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "reviewer",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Performance> reviewedPerformances = new ArrayList<Performance>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Performance> performanceList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Payroll> payrollList = new ArrayList<Payroll>();

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LeaveBalance> leaveBalances = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ElementCollection
    @CollectionTable(
            name = "employee_attributes",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @MapKeyJoinColumn(name = "attribute_id")
    @Column(name = "attribute_value")
    @JsonSerialize(using = AttributesSerializer.class)
    private Map<EmployeeAttribute, String> attributes = new HashMap<>();

    @ManyToMany
    @JoinTable(
            name = "employee_designation",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "designation_id")
    )
    private List<Designation> designations = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "employee_skill",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

}
