package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

@Getter()
@RequiredArgsConstructor
@EqualsAndHashCode()
public class CyclicalServiceDto {

//TODO maybe in the future :>
//    private final Optional<
//            @Min(value = 0L, message = "Specified Business Id must be a positive Integer")
//            Integer> businessId;
    @NotNull(message = "Nie wybrano firmy !!!")
    @Min(value = 0L, message = "Id firmy powinno być liczbą całkowitą większą od zera")
    private final Integer businessId;

//    @NotNull(message = "signatureType is empty !!!")
//    @Min(value = 0L, message = "Niepoprawny typ podpisu")
//    @Max(value = 3L, message = "Niepoprawny typ podpisu")
//    private final Integer signatureType;

//    @FutureOrPresent(message = "future or present constraint broken")
    @NotNull(message = "Nie określono początkowej daty !!!")
    private final LocalDateTime cycleStart;

    @Min(value = 1L, message = "Certyfikat nie może trwać krócej niż rok")
    @NotNull(message = "Nie podano długości trwania certyfikatu")
    private final Integer certificateLengthInYears;

    @NotBlank(message = "Nie podano numeru karty !!!")
    @Size(max=40, message = "Podany numer karty jest za długi !!!")
    @Pattern(regexp = "^[\\d\\s]+$", message = "Numer kart może zawierać tylko cyfry i spacje !!!")
    private final String  cardNumber;

    @NotNull(message = "Nie określono typu karty !!!")
    private final Integer  cardType;


    @NotBlank(message = "Nie określono numeru seryjnego certyfikatu !!!")
    @Size(max=40, message = "Podany numery seryjny jest za długi !!!")
    @Pattern(regexp = "^[0-9ABCDEF]+$", message = "Numer seryjny certyfikatu może zawierać tylko liczby!!!")
    private final String  certSerialNumber;


    private final Optional<
            @Size(max = 80, message = "Określona nazwa w organizacji jest za długa")
                    String> nameInOrganisation;


    @NotNull(message = "Nie określono pola jednorazowy !!!")
    private final Boolean oneTime;





    @NotNull(message = "Nie określono użytkownika !!!")
    @Min(value = 0L, message = "Id użytkownika powinno być liczbą całkowitą większą od zera")
    private final Integer serviceUserId;


    @NotBlank(message = "Nie określono numeru zgody !!!")
    @Size(max=40, message = "Podany numer zgody jest za długi !!!")
    private final String  agreementNumber;

    @Size(max=255, message = "Podany opis usługi jest za długi  !!!")
    private final String description;

//    @Min(value = 0L, message = "Specified relatedAccountId must be a positive Integer")
//    @NotNull(message = "relatedAccountId is empty !!!")
//    private final Integer relatedAccountId;




}
