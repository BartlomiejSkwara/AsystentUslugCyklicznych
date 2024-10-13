package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import com.example.uslugicykliczne.repo.PhoneNumberRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PhoneNumberService {
    private final PhoneNumberRepo phoneNumberRepo;

    public PhoneNumberService(PhoneNumberRepo PhoneNumberRepo) {
        this.phoneNumberRepo = PhoneNumberRepo;
    }

    public void insertNewPhoneNumberEntities(List<String> PhoneNumbers, ContactDataEntity contactDataEntity){
        ArrayList<PhoneNumberEntity> phoneNumberEntities = new ArrayList<>();
        for(String PhoneNumber:PhoneNumbers){
            PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
            phoneNumberEntity.setNumber(PhoneNumber);
            phoneNumberEntity.setContactDataEntity(contactDataEntity);
            phoneNumberEntities.add(phoneNumberEntity);
        }
        phoneNumberRepo.saveAll(phoneNumberEntities);

    }

    public void updatePhoneNumberEntities(List<PhoneNumberEntity> phoneNumberEntities, List<String> phoneNumbers, ContactDataEntity contactDataEntity) {

        int smallerSize  = phoneNumberEntities.size();
        if(phoneNumbers.size()<phoneNumberEntities.size()){
            List<PhoneNumberEntity> entities = new ArrayList<>(phoneNumberEntities.subList(phoneNumbers.size(), phoneNumberEntities.size()));
            contactDataEntity.removePhoneNumber(entities);
            phoneNumberRepo.deleteAll(entities);
            smallerSize = phoneNumbers.size();
        }
        else if (phoneNumbers.size()>phoneNumberEntities.size()){
            insertNewPhoneNumberEntities(phoneNumbers.subList(phoneNumberEntities.size(), phoneNumbers.size()),contactDataEntity);
            smallerSize = phoneNumberEntities.size();
        }

        for (int i = 0; i<smallerSize; i++){
            phoneNumberEntities.get(i).setNumber(phoneNumbers.get(i));
        }

        phoneNumberRepo.saveAll(phoneNumberEntities.subList(0,smallerSize));
    }
}
