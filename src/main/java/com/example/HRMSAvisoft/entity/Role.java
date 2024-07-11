package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {
    @Id
    private String role;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role"))
    @Enumerated(EnumType.STRING)
    @Column(name = "privilege")
    private Set<Privilege> privilege = new HashSet<>();
}
