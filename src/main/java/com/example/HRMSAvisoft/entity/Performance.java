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
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long performanceId;

    @ManyToOne
    private Employee employee;

    private String reviewDate;

    @ManyToOne()
    @JoinColumn(name = "reviewerId", referencedColumnName = "employeeId")
    private Employee reviewer;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    private String comment;

}
