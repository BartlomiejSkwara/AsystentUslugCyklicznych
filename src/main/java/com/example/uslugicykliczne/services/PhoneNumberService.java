package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import com.example.uslugicykliczne.repo.PhoneNumberRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepo phoneNumberRepo;

    public PhoneNumberService(PhoneNumberRepo PhoneNumberRepo) {
        this.phoneNumberRepo = PhoneNumberRepo;
    }

    public void insertNewPhoneNumberEntities(Collection<String> PhoneNumbers, ContactDataEntity contactDataEntity){
        ArrayList<PhoneNumberEntity> phoneNumberEntities = new ArrayList<>();
        for(String PhoneNumber:PhoneNumbers){
            PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
            phoneNumberEntity.setNumber(PhoneNumber);
            phoneNumberEntity.setContactDataEntity(contactDataEntity);
            phoneNumberEntities.add(phoneNumberEntity);
        }
        phoneNumberRepo.saveAll(phoneNumberEntities);

    }
}
