package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Address_Zipcode")
public class Zipcode {
    @Id
    private Long zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

}
