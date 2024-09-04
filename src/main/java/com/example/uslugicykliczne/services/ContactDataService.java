package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.repo.ContactDataRepo;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ContactDataService {
    private final ContactDataRepo contactDataRepo;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    public ContactDataService(ContactDataRepo contactDataRepo, EmailService emailService, PhoneNumberService phoneNumberService) {
        this.contactDataRepo = contactDataRepo;
        this.emailService = emailService;
        this.phoneNumberService = phoneNumberService;
    }


    public ContactDataEntity insertBlankContactDataEntity(){
        return contactDataRepo.save(new ContactDataEntity());
    }

    public ContactDataEntity insertContactDataEntity(Collection<String> emails, Collection<String> phoneNumbers){
        ContactDataEntity contactDataEntity = insertBlankContactDataEntity();
        emailService.insertNewEmailEntities(emails,contactDataEntity);
        phoneNumberService.insertNewPhoneNumberEntities(phoneNumbers,contactDataEntity);
        return contactDataEntity;
    }
}
