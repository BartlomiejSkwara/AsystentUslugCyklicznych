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
    @NotNull(message = "Business Id  is empty !!!")
    @Min(value = 0L, message = "Specified Business Id must be a positive Integer")
    private final Integer businessId;

//    @NotNull(message = "signatureType is empty !!!")
//    @Min(value = 0L, message = "Niepoprawny typ podpisu")
//    @Max(value = 3L, message = "Niepoprawny typ podpisu")
//    private final Integer signatureType;

//    @FutureOrPresent(message = "future or present constraint broken")
    @NotNull(message = "cycleStart is empty !!!")
    private final LocalDateTime cycleStart;

    @Min(value = 1L, message = "Certyfikat nie może trwać krócej niż rok")
    @NotNull(message = "certificateLengthInYears is empty !!!")
    private final Integer certificateLengthInYears;

    @NotBlank(message = "Certificate cardNumber is empty !!!")
    @Size(max=40, message = "Certificate cardNumber is too long !!!")
    private final String  cardNumber;

    @NotBlank(message = "Certificate cardType is empty !!!")
    @Size(max=40, message = "Certificate cardType is too long !!!")
    private final String  cardType;


    @NotBlank(message = "Certificate serialNumber is empty !!!")
    @Size(max=40, message = "Certificate serialNumber is too long !!!")
    private final String  certSerialNumber;


    private final Optional<
            @Size(max = 80, message = "Specified nameInOrganisation is too long")
                    String> nameInOrganisation;


    @NotNull(message = "oneTime not set !!!")
    private final Boolean oneTime;



    @NotNull(message = "Price is empty !!!")
    @Min(value = 0, message = "Can't specify negative price !!!")
    private final Double price;

    @NotNull(message = "serviceUserId is empty !!!")
    @Min(value = 0L, message = "Specified serviceUserId must be a positive Integer")
    private final Integer serviceUserId;


    @NotBlank(message = "Agreement Number is empty !!!")
    @Size(max=40, message = "Agreement Number is too long !!!")
    private final String  agreementNumber;

    @NotBlank(message = "Description is empty !!!")
    @Size(max=255, message = "Description is too long !!!")
    private final String description;

    @Min(value = 0L, message = "Specified relatedAccountId must be a positive Integer")
    @NotNull(message = "relatedAccountId is empty !!!")
    private final Integer relatedAccountId;




}
