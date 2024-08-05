package com.example.HRMSAvisoft.attribute;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
public class OrganizationAttribute extends Attribute
{
    public OrganizationAttribute(String attributeKey) {
        super(attributeKey);
    }
}
