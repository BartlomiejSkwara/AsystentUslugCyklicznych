package com.example.uslugicykliczne.dataTypes;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public enum StatusEnum {
    AWAITING_RENEWAL (1 << 0, "Oczekuje Odnowienia"),
    PRO_FORM_SENT(1<<1, "Pro Forma wysłana"),
    MARKED_FOR_CANCEL(1<<2, "Oznaczony do anulowania"),
    CANCELED(1<<3, "Anulowany"),
    MARKED_AS_NON_RENEWABLE(1<<4, "Oznaczony jako nieodnawialny"),
    RENEWED_ELSEWHERE(1<<5,"Odnowiony gdzie indziej"),
    PAYMENT_DONE(1<<6, "Zapłata otrzymana"),
    INVOICE_SENT(1<<7,"Faktura wystawiona, Kod wysłany"),
    RENEWED(1<<8,"Odnowiony");


    private final int maskValue;
    private final String statusName;

    public int getMaskValue() {
        return maskValue;
    }

    public String getStatusName() {
        return statusName;
    }
}
