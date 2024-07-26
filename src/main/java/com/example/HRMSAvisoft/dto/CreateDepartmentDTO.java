package com.example.HRMSAvisoft.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentDTO {

    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Invalid department name")
    String department;

    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Invalid department description")
    String description;

    Long managerId;

    Long organizationId;

}
