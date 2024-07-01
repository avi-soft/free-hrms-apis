package com.example.HRMSAvisoft.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "property_number")
    private String propertyNumber;

    @ManyToOne
    @JoinColumn(name = "zip_code_id")
    private Zipcode zipCode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(name = "country")
    private String country;

}
