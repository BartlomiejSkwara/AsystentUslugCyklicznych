package com.example.uslugicykliczne.dataTypes;

import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;

import java.util.List;

public record ContactDataRecordProjeciton(
        List<String> emails,
        List<String> phoneNumbers
) {
}
