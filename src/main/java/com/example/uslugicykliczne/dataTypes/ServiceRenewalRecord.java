package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Optional;

public record ServiceRenewalRecord(
        @Min(value = 1L, message = "Certyfikat nie może trwać krócej niż rok")
        @NotNull(message = "certificateLengthInYears is empty !!!")
        Integer certificateLengthInYears,
        @NotBlank(message = "Certificate cardNumber is empty !!!")
        @Size(max=40, message = "Certificate cardNumber is too long !!!")
        String  cardNumber,

        @NotBlank(message = "Certificate cardType is empty !!!")
        @Size(max=40, message = "Certificate cardType is too long !!!")
        String  cardType,
        @NotBlank(message = "Certificate serialNumber is empty !!!")
        @Size(max=40, message = "Certificate serialNumber is too long !!!")
        String  certSerialNumber,
        Optional<
                @Size(max = 80, message = "Specified nameInOrganisation is too long")
                        String> nameInOrganisation
) {
}
