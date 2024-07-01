package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NewUserResponseDTO {
    private Long userId;
    private String email;
    private String createdAt;
    private Long employeeId;
}
