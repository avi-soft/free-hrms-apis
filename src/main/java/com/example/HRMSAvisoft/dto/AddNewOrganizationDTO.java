package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddNewOrganizationDTO
{
    private Long organizationId;
    @NotNull(message = "Name of Organization cannot be null")
    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Name of organization cannot be empty")
    private String organizationName;

    @NotNull(message = "Description of Organization cannot be null")
    @Size(min=1,max =200,message = "min character is 1 and maximum characters can be upto 200 for Description of Organization")
    @Pattern(regexp = "^[^\\s].*$", message = "Description of organization cannot be empty")
    private String organizationDescription;

}
