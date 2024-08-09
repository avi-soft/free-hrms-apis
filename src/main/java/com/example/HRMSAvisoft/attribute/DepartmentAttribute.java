package com.example.HRMSAvisoft.attribute;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
public class DepartmentAttribute extends Attribute{

    public DepartmentAttribute(String attributeKey){
        super(attributeKey);
    }

}
