package com.example.HRMSAvisoft.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class CreateBranchDTO {
    private String branchName;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<String, String> attributes;

    private Long organizationId;
}
