package com.example.HRMSAvisoft.attribute;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
public abstract class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long attributeId;

    @Column(nullable = false, unique = true)
    private String attributeKey;

    public Attribute(String attributeKey) {
        this.attributeKey = attributeKey;
    }
}