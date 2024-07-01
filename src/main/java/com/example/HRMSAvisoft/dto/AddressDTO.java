package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotBlank(message = "Property Number should not be empty")
    private String propertyNumber;
    private AddressType addressType;
    @NotNull(message ="Invalid Zip Code :Zip Code field is empty " )
    private Long zipCode;
    @NotBlank(message = "Invalid City: City is empty!!")
    private String city;
    @NotBlank(message = "Invalid State: State is empty!!")
    private String state;
    @NotBlank(message = "Invalid Country: Country is empty!! ")
    private String country;

}
