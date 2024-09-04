package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.repo.EmailRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class EmailService {
    private final EmailRepo emailRepo;

    public EmailService(EmailRepo emailRepo) {
        this.emailRepo = emailRepo;
    }

    public void insertNewEmailEntities(Collection<String> emails, ContactDataEntity contactDataEntity){
        ArrayList<EmailEntity> emailEntities = new ArrayList<>();
        for(String email:emails){
            EmailEntity emailEntity = new EmailEntity();
            emailEntity.setEmail(email);
            emailEntity.setContactDataEntity(contactDataEntity);
            emailEntities.add(emailEntity);
        }
        emailRepo.saveAll(emailEntities);

    }
}
