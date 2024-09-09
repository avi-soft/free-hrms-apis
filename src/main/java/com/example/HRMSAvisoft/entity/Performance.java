package com.example.HRMSAvisoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_employee_id")
    @JsonIgnore
    private Employee employee;

    private String reviewDate;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", referencedColumnName = "employeeId")
    @JsonIgnore
    private Employee reviewer;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    private String comment;

}
