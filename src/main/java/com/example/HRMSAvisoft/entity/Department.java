package com.example.HRMSAvisoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "departments", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Organization> organizations = new HashSet<>();

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "managerId")
    private Employee manager;
}
