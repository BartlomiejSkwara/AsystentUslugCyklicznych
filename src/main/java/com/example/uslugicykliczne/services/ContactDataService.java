package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.repo.ContactDataRepo;
import com.example.uslugicykliczne.repo.EmailRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactDataService {
    private final ContactDataRepo contactDataRepo;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    private final EmailRepo emailRepo;
    public ContactDataService(ContactDataRepo contactDataRepo, EmailService emailService, PhoneNumberService phoneNumberService, EmailRepo emailRepo) {
        this.contactDataRepo = contactDataRepo;
        this.emailService = emailService;
        this.phoneNumberService = phoneNumberService;
        this.emailRepo = emailRepo;
    }


    public ContactDataEntity insertBlankContactDataEntity(){
        return contactDataRepo.save(new ContactDataEntity());
    }

    public ContactDataEntity insertContactDataEntity(List<String> emails, List<String> phoneNumbers){
        ContactDataEntity contactDataEntity = insertBlankContactDataEntity();
        emailService.insertNewEmailEntities(emails,contactDataEntity);
        phoneNumberService.insertNewPhoneNumberEntities(phoneNumbers,contactDataEntity);
        return contactDataEntity;
    }

    public void  updateContactDataEntity(ContactDataEntity updatedEntity,List<String> emails, List<String> phoneNumbers){


        emailService.updateEmailEntities(updatedEntity.getEmails(),emails,updatedEntity);
        phoneNumberService.updatePhoneNumberEntities(updatedEntity.getPhoneNumbers(),phoneNumbers,updatedEntity);
        //updatedEntity.setEmails(updatedEmails);

    }
}
