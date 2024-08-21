package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.attribute.BranchAttribute;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AttributesSerializer;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BranchResponseDTO {

    private String branchName;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<BranchAttribute, String> attributes;

    private Long organizationId;

    private String organizationName;

    private String organizationImage;

    private String organizationDescription;

}
