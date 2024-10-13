package com.example.uslugicykliczne.dataTypes;

import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;

import java.util.List;

public record BusinessRecordProjection(
        Integer idBusiness,
        String name,
        String adres,
        String regon,
        String nip,
        String comments,
        List<EmailEntity> emails,
        List<PhoneNumberEntity> phoneNumbers
) {
}
