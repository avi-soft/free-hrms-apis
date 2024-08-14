package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String branchAddress;
    private String branchCity;
    private String branchState;
    private String branchCountry;
    private String branchZipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}
