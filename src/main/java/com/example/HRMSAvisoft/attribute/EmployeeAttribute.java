package com.example.HRMSAvisoft.attribute;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
public class EmployeeAttribute extends Attribute
{
public EmployeeAttribute(String attributeKey) {
    super(attributeKey);
}
}
