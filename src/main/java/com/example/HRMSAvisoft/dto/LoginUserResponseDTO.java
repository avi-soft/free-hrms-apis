package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.attribute.Attribute;
import com.example.HRMSAvisoft.attribute.EmployeeAttribute;
import com.example.HRMSAvisoft.entity.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Attr;
import utils.AttributesSerializer;
//import utils.EmployeeAttributesSerializer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserResponseDTO {
    private Long userId;
    private Long employeeId;
    private String employeeCode;
    private Boolean active;
//    private String email;
    private String createdAt;
    private Set<Role> roles;
    private Long managerId;
    private String firstName;
    private String lastName;
//    private String contact;
    private List<Address> addresses;
//    private String adhaarNumber;
//    private String panNumber;
//    private String uanNumber;
//    private Position position;
//    private String joinDate;
    private Gender gender;
    private String profileImage;
//    private String dateOfBirth;
//    private BigDecimal salary;
//    private Account account;

    @JsonSerialize(using = AttributesSerializer.class)
    private Map<EmployeeAttribute, String> attributes = new HashMap<>();

    Set<Department> departments;
}
