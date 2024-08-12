package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import utils.AttributesSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateOrganizationDTO
{
    private Long organizationId;
    @Size(min=1,max = 50,message = "min character is 1 and maximum characters can be upto 50")
    @Pattern(regexp = "^[^\\s].*$", message = "Name of organization cannot be empty")
    private String organizationName;

    @Size(min=1,max =200,message = "min character is 1 and maximum characters can be upto 200 for Description of Organization")
    @Pattern(regexp = "^(?=\\s*\\S)(?:[\\s\\S]{1,200})$", message = "Description of organization cannot be empty")
    private String organizationDescription;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<String, String> attributes = new HashMap<>();
}
