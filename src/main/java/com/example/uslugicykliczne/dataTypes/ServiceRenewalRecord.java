package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Optional;

public record ServiceRenewalRecord(
        @FutureOrPresent(message = "future or present constraint broken")
        @NotNull(message = "cycleStart is empty !!!")
        LocalDateTime cycleStart,
        @FutureOrPresent(message = "future or present constraint broken")
        @NotNull(message = "cycleEnd is empty !!!")
        LocalDateTime cycleEnd,

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
