package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter()
@RequiredArgsConstructor
@EqualsAndHashCode
public class ServiceUserDTO {
    @NotBlank(message = "Login field is empty !!!")
    @Size(max=40, message = "Login is too long !!!")
    private final String  login;

    @NotBlank(message = "Password field is empty!!!")
    @Size(max=80, message = "Password is too long !!!")
    private final String password;

    @NotBlank(message = "ServiceUser's name is empty !!!")
    @Size(max=40, message = "ServiceUser's name is too long !!!")
    private final String  name;

    @NotBlank(message = "ServiceUser's surname is empty!!!")
    @Size(max=40, message = "ServiceUser's surname is too long !!!")
    private final String surname;

    @NotEmpty(message = "No emails specified for ServiceUser ")
    private final List<
                @NotBlank(message = "ServiceUser's email is empty !!!")
                @Size(max=40, message = "ServiceUser's email is too long !!!")
                @Email(message = "ServiceUser's email is not a correct email !!!")
                String> emails;

    @NotEmpty(message = "No phone number specified for ServiceUser ")
    private final List<
            @NotBlank(message = "ServiceUser's phone number is empty!!!")
            @Size(max = 16 , message = "ServiceUser's phone number is too long !!!")
            @Pattern(regexp = "^\\d+$", message = "ServiceUser's phone number has characters other than numbers!!!")
            String> phoneNumbers;

    @NotNull(message = "hasPolishPESEL not set !!!")
    private final Boolean hasPolishPESEL;

    private final String comments;


    private final Optional<
            @Pattern(regexp = "^\\b[0-9]{11}\\b$", message = "Check pesel's length and if it consists only of numbers!!!")
            String> taxId;



}
