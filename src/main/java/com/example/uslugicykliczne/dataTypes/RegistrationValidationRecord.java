package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationValidationRecord(
        @NotBlank(message = "Login field is empty !!!")
        @Size(max=40, message = "Login is too long !!!")
        String  login,
        @NotBlank(message = "Password field is empty!!!")
        @Size(max=80, message = "Password is too long !!!")
        String password

) {
}
