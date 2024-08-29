package com.example.HRMSAvisoft.entity;
import com.example.HRMSAvisoft.attribute.OrganizationAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import utils.AttributesSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Builder
public class Organization
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long organizationId;
    @Column(nullable = false)
    private String organizationName;
    private String organizationImage;
    private String organizationDescription;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "organization_department",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private Set<Department> departments = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "organization_branch",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private Set<Branch> branches = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "organization_attributes",
            joinColumns = @JoinColumn(name = "organization_id")
    )
    @MapKeyJoinColumn(name = "attribute_id")
    @Column(name = "attribute_value")
    @JsonSerialize(using = AttributesSerializer.class)
    private Map<OrganizationAttribute, String> attributes = new HashMap<>();
}
