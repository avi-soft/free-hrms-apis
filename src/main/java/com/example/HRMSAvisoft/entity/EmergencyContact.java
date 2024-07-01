package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long emergencyContactId;

    @NotBlank(message = "Contact field cannot be empty")
    String contact;

    @NotBlank(message = "Relationship field cannot be empty")
    @Column(nullable = false)
    String relationship;

}
