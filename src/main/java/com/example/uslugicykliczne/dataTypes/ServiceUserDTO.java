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
    @NotBlank(message = "Pole login jest puste !!!")
    @Size(max=40, message = "Podany login jest za długi !!!")
    private final String  login;

//    @NotBlank(message = "Pole hasło jest puste !!!")
    @Size(max=80, message = "Podane hasło jest za długie !!!")
    private final String password;

    @NotBlank(message = "Pole imie użytkownika jest puste !!!")
    @Size(max=40, message = "Podane imie użytkownika jest za długie !!!")
    private final String  name;

    @NotBlank(message = "Pole nazwisko użytkownika jest puste!!!")
    @Size(max=40, message = "Podane nazwisko użytkownika jest za długie !!!")
    private final String surname;

    private final List<
                @NotBlank(message = "Podano pusty email !!!")
                @Size(max=40, message = "Podany email jest za długi !!!")
                @Email(message = "Podany email nie jest poprawny !!!")
                String> emails;

    private final List<
            @NotBlank(message = "Podano pusty numer telefonu")
            @Size(max = 20 , message = "Podany numer telefonu jest za długi")
            @Pattern(regexp = "^\\+\\d+ [\\d\\s()-]+$", message = "Numer telefonu może zawierać tylko liczby!!!")
            String> phoneNumbers;

    @NotNull(message = "Nie określono pola ma polski PESEL")
    private final Boolean hasPolishPESEL;

    @Size(max=255, message = "Podany komentarz jest za długi  !!!")
    private final String comments;


    private final Optional<
            @Pattern(regexp = "^\\b[0-9]{11}\\b$", message = "Sprawdź czy PESEL ma odpowiednią długość i czy składa się wyłącznie z cyfr !!!")
            String> taxId;



}
