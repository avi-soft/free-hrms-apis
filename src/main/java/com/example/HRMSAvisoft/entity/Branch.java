package com.example.HRMSAvisoft.entity;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.example.HRMSAvisoft.attribute.DepartmentAttribute;
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

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Employee> employees = new HashSet<>();

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
