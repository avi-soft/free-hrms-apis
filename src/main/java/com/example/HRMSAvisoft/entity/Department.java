package com.example.HRMSAvisoft.entity;

import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.*;

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

//    @Column(nullable = false)
    private String department;

    private String description;

    @ElementCollection
    @CollectionTable(
            name = "department_attributes",
            joinColumns = @JoinColumn(name = "department_id")
    )
    @MapKeyJoinColumn(name = "attribute_id")
    @Column(name = "attribute_value")
    @JsonSerialize(using = AttributesSerializer.class)
    private Map<DepartmentAttribute, String> attributes = new HashMap<>();

    @ManyToMany(mappedBy = "departments", fetch = FetchType.EAGER)
    private Set<Organization> organizations = new HashSet<>();

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "managerId")
    private Employee manager;
}
