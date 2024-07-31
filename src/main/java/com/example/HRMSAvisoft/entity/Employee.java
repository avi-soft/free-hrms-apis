package com.example.HRMSAvisoft.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@JsonFormat
@Builder
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
    private List<EmergencyContact> emergencyContacts = new ArrayList<EmergencyContact>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<Address>();

    //    @Enumerated(EnumType.STRING)
    //    private Position position;
    //
    //    private String joinDate;
    //
    //    @Enumerated(EnumType.STRING)
    //    private Gender gender;
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
    
    @ElementCollection
    @CollectionTable(
            name = "employee_attributes",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @MapKeyJoinColumn(name = "attribute_id")
    @Column(name = "attribute_value")
    private Map<EmployeeAttribute, String> attributes = new HashMap<>();
}
