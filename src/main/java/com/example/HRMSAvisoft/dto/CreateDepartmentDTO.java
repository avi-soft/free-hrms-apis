package com.example.HRMSAvisoft.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentDTO {

    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Invalid department name")
    private String department;

    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^(?=\\s*\\S)(?:[\\s\\S]{1,200})$", message = "Invalid department description")
    private String description;

    private Long managerId;

    private Long organizationId;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<String, String> attributes;

}
