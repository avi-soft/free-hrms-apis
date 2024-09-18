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
public class BranchAttribute extends Attribute{
    public BranchAttribute(String attributeKey){
        super(attributeKey);
    }
}
