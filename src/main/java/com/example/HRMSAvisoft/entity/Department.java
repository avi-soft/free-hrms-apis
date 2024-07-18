package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long departmentId;

    @Column(nullable = false)
    private String department;

    private String description;

    // One-to-One mapping with Organization
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "organization_id", referencedColumnName = "organizationId")
    private Organization organization;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "managerId")
    private Employee manager;
}
