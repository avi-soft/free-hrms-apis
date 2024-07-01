package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePerformanceDTO {

    private Rating rating;

    private String comment;

}
