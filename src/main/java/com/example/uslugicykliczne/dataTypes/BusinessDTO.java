package com.example.uslugicykliczne.dataTypes;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor()
@Getter()
@EqualsAndHashCode()
public class BusinessDTO {

    @NotBlank(message = "Pole nazwa firmy jest puste !!!")
    @Size(max=80, message = "Nazwa firmy jest za długa !!!")
    private final String  name;



    @NotBlank(message = "Pole kod pocztowy jest puste !!!")
    @Size(max=20, message = "Kod pocztowy jest za długi !!!")
    private String postalCode;

    @NotBlank(message = "Pole miejscowość jest puste !!!")
    @Size(max=40, message = "Nazwa miejscowości jest za długa !!!")
    private String locality;

    @NotBlank(message = "Pole ulica jest puste !!!")
    @Size(max=40, message = "Nazwa ulicy jest za długa !!!")
    private String street;


    @NotNull(message = "Pole nr posesji jest puste !!!")
    private Integer propertyNumber;


    @NotNull(message = "Pole nr lokalu jest puste !!!")
    private Integer apartmentNumber;



    @NotBlank(message = "Pole nip jest puste !!!")
    @Size(max=40, message = "Nip jest za długi !!!")
    private final String  nip;

    private final Optional<
            @Size(max=40, message = "Regon jest za długi !!!")
            String> regon;


    @NotEmpty(message = "Nie określono żadnego adresu email dla firmy ")
    private final List<
            @NotBlank(message = "Podano pusty email !!!")
            @Size(max=40, message = "Podany email jest za długi !!!")
            @Email(message = "Podany email nie jest poprawny !!!")
                    String> emails;

    @NotEmpty(message = "Nie określono żadnego numeru telefonu dla firmy")
    private final List<
            @NotBlank(message = "Podano pusty numer telefonu")
            @Size(max = 16 , message = "Podany numer telefonu jest za długi")
            @Pattern(regexp = "^\\d+$", message = "Numer telefonu może zawierać tylko liczby!!!")
                    String> phoneNumbers;



    private final String comments;


}