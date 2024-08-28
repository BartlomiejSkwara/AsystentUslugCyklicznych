package com.example.uslugicykliczne.dataTypes;

import java.time.LocalDateTime;
import java.time.Period;

public interface CyclicalServiceProjection {
    Integer getId();
    String getDescription();
    Double getPrice();
    LocalDateTime getFirstCycleStart();
    LocalDateTime getNextRenewal();
    Period getRenewalPeriod();

    CustomerProjection getCustomerEntity();
    DysponentProjection getDysponentEntity();
    public interface CustomerProjection {
        Integer getId();
        String getName();
        String getSurname();
        String getEmail();
        String getPhoneNumber();
    }

    public interface DysponentProjection{
        Integer getId();
        String getMfnSerialNumber();
        String getEmail();
        String getPhoneNumber();
        String getName();
        String getSurname();
    }

}
