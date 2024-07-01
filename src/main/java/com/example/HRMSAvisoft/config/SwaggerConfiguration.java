package com.example.HRMSAvisoft.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
@OpenAPIDefinition(info=@Info(title = "HRMS Avisoft APIs",
        description = "These apis are for HRMS Avisoft Project",
        summary = "Apis contains Summary Info",

        version = "api/v1"
)

        ,security = @SecurityRequirement(name = "hrmsSecurity")
)
@SecurityScheme(name = "hrmsSecurity",in = SecuritySchemeIn.HEADER,type = SecuritySchemeType.HTTP,bearerFormat = "JWT",scheme = "bearer",description = "This is security for HRMS application")

public class SwaggerConfiguration {


}
