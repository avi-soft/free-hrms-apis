package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@ToString
public class EmployeeAttribute
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long attributeId;

    @Column(nullable = false, unique = true)
    private String attributeKey;
}
