package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeUserRoleDTO
{
    private Role oldRole;
    private Role newRole;
}
