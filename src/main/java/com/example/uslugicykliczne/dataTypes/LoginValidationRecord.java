package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginValidationRecord(
        @NotBlank(message = "Login field is empty !!!")
        @Size(max=40, message = "Login is too long !!!")
        String  login,
        @NotBlank(message = "Password field is empty!!!")
        String password
) {
}
