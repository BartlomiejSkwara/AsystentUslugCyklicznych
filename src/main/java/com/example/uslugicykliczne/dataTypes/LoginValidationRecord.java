package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginValidationRecord(
        @NotBlank(message = "Nie podano loginu !!!")
        @Size(max=40, message = "Podany login jest za długi !!!")
        String  login,
        @NotBlank(message = "Nie podano hasła !!!")
        @Size(max=40, message = "Podane hasło jest za długie !!!")
        String password
) {
}
