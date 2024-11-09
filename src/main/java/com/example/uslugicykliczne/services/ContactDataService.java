package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import com.example.uslugicykliczne.repo.ContactDataRepo;
import com.example.uslugicykliczne.repo.EmailRepo;
import com.example.uslugicykliczne.repo.PhoneNumberRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ContactDataService {
    private final ContactDataRepo contactDataRepo;
    private final EmailService emailService;
    private final PhoneNumberService phoneNumberService;
    private final EmailRepo emailRepo;
    private final PhoneNumberRepo phoneNumberRepo;

    public ContactDataService(ContactDataRepo contactDataRepo, EmailService emailService, PhoneNumberService phoneNumberService, EmailRepo emailRepo, PhoneNumberRepo phoneNumberRepo) {
        this.contactDataRepo = contactDataRepo;
        this.emailService = emailService;
        this.phoneNumberService = phoneNumberService;
        this.emailRepo = emailRepo;
        this.phoneNumberRepo = phoneNumberRepo;
    }


    public ContactDataEntity insertBlankContactDataEntity(){
        return contactDataRepo.save(new ContactDataEntity());
    }

    public ContactDataEntity insertContactDataEntity(List<String> emails, List<String> phoneNumbers){
        ContactDataEntity contactDataEntity = insertBlankContactDataEntity();
        if (emails == null)
            emails = new ArrayList<>();
        if(phoneNumbers == null)
            phoneNumbers = new ArrayList<>();

        emailService.insertNewEmailEntities(emails,contactDataEntity);
        phoneNumberService.insertNewPhoneNumberEntities(phoneNumbers,contactDataEntity);
        return contactDataEntity;
    }

    public void  updateContactDataEntity(ContactDataEntity updatedEntity,List<String> emails, List<String> phoneNumbers){

        if (emails == null)
            emails = new ArrayList<>();
        if(phoneNumbers == null)
            phoneNumbers = new ArrayList<>();

        emailService.updateEmailEntities(updatedEntity.getEmails(),emails,updatedEntity);
        phoneNumberService.updatePhoneNumberEntities(updatedEntity.getPhoneNumbers(),phoneNumbers,updatedEntity);
        //updatedEntity.setEmails(updatedEmails);

    }

    public HashSet<String> findContactsInDB(List<String> emails, List<String> phoneNumbers) {
        HashSet<String> contactSet = new HashSet<>();

        contactSet.addAll(emailRepo.findAllLike(emails));
        contactSet.addAll(phoneNumberRepo.findAllLike(phoneNumbers));

//        if(contactSet.size() == 0){
//            return null;
//        }
//
//        StringBuilder stringBuilder = new StringBuilder("");
//
//        for (String contact : contactSet) {
//            stringBuilder.append(contact);
//            stringBuilder.append(", ");
//        }
//        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        return contactSet;
    }
}
