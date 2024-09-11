package com.example.uslugicykliczne.dataTypes;

import java.time.LocalDateTime;

public record CertificateProjectionRecord(
    int idCertificate,
    String certificateSerialNumber,
    LocalDateTime validFrom,
    LocalDateTime validTo,
    String cardType,
    String cardNumber,
    String nameInOrganisation
) {
}
