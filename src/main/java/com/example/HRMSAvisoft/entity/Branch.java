package com.example.HRMSAvisoft.entity;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import utils.AttributesSerializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long branchId;

    @Column(nullable = false)
    private String branchName;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "branch_department",
            joinColumns = @JoinColumn(name = "branch_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "branch_attributes",
            joinColumns = @JoinColumn(name = "branch_id")
    )
    @MapKeyJoinColumn(name = "attribute_id")
    @Column(name = "attribute_value")
    @JsonSerialize(using = AttributesSerializer.class)
    private Map<BranchAttribute, String> attributes = new HashMap<>();

    @ManyToMany(mappedBy ="branches", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Organization> organizations = new HashSet<>();
}
