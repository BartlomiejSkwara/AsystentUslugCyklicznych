package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationValidationRecord(
        @Min(value = 0, message = "Role field contains incorrect value")
        @NotNull
        Integer role,
        @NotBlank(message = "Login field is empty !!!")
        @Size(max=40, message = "Login is too long !!!")
        String  login,
        @NotBlank(message = "Password field is empty!!!")
        @Size(max=80, message = "Password is too long !!!")
        String password

) {
}
