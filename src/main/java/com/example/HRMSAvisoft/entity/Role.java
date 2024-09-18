    package com.example.HRMSAvisoft.entity;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.HashSet;
    import java.util.Set;

    import java.util.HashSet;
    import java.util.Set;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "roles")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false)
        private Long roleId;

        private String role;

        @ElementCollection(fetch = FetchType.EAGER)
        @CollectionTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role"))
        @Enumerated(EnumType.STRING)
        @Column(name = "privilege")
        private Set<Privilege> privilege = new HashSet<>();
    }