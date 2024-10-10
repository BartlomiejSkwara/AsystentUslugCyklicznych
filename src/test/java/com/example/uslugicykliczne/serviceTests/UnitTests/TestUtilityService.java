package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.entity.*;
import com.example.uslugicykliczne.repo.ContactDataRepo;
import com.example.uslugicykliczne.repo.EmailRepo;
import com.example.uslugicykliczne.repo.PhoneNumberRepo;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class TestUtilityService {


    static public CertificateEntity createCertificateEntity(CyclicalServiceEntity cyclicalServiceEntity,LocalDateTime validTo, boolean renewed, boolean renewalMessageSent) {
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setCertificateSerialNumber("123");
        certificateEntity.setCyclicalServiceEntity(cyclicalServiceEntity);
        certificateEntity.setCardNumber("123");
        certificateEntity.setCardType("physical");
        certificateEntity.setValidTo(validTo);
        certificateEntity.setValidFrom(LocalDateTime.now());
        certificateEntity.setNameInOrganisation("CEO");
        certificateEntity.setMostRecent(renewed);
        certificateEntity.setRenewalMessageSent(renewalMessageSent);
        return certificateEntity;
    }

    static public CyclicalServiceEntity createCyclicalServiceEntity(boolean oneTime, ServiceUserEntity serviceUser, BusinessEntity businessEntity){
        CyclicalServiceEntity cyclicalServiceEntity = new CyclicalServiceEntity();
        cyclicalServiceEntity.setDescription("service desc");
        cyclicalServiceEntity.setPrice(100);
        cyclicalServiceEntity.setOneTime(oneTime);
        cyclicalServiceEntity.setAgreementNumber("1234");
        ContactDataEntity contactDataEntity;
        if (serviceUser!=null)
            contactDataEntity = serviceUser.getContactData();
        else if (businessEntity!=null)
            contactDataEntity = businessEntity.getContactData();
        else
            contactDataEntity = createContactData();

        if(serviceUser==null)
            cyclicalServiceEntity.setServiceUser(createServiceUserEntity(contactDataEntity));
        else
            cyclicalServiceEntity.setServiceUser(serviceUser);

        if (businessEntity==null)
            cyclicalServiceEntity.setBusiness(createBusinessEntity(contactDataEntity));
        else
            cyclicalServiceEntity.setBusiness(businessEntity);

        cyclicalServiceEntity.setBusiness(businessEntity);

        return cyclicalServiceEntity;
    }

    static public ServiceUserEntity createServiceUserEntity(ContactDataEntity contactData)
    {
        var serviceUser = new ServiceUserEntity();
        serviceUser.setName("name");
        serviceUser.setSurname("surname");
        serviceUser.setComments("comment");
        serviceUser.setHasPolishPesel(false);

        if (contactData==null)
            serviceUser.setContactData(createContactData());
        else
            serviceUser.setContactData(contactData);



        return serviceUser;

    }
    static public ContactDataEntity createContactData(){
        var contactData = new ContactDataEntity();
        return contactData;
    }

    static public EmailEntity createEmailData(ContactDataEntity contactData){
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setEmail("mail");
        emailEntity.setContactDataEntity(contactData);
        return emailEntity;
    }

    static public PhoneNumberEntity createPhoneNumberEntity(ContactDataEntity contactData){
        PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
        phoneNumberEntity.setNumber("123");
        phoneNumberEntity.setContactDataEntity(contactData);
        return phoneNumberEntity;
    }

    static public BusinessEntity createBusinessEntity(ContactDataEntity contactDataEntity){
        BusinessEntity entity = new BusinessEntity();
        entity.setRegon("12314");
        entity.setNip("521124");
        entity.setAdres("bruhowa 33");
        entity.setName("bruhcorp");
        entity.setComments("comment");
        if (contactDataEntity==null)
            entity.setContactData(createContactData());
        else
            entity.setContactData(contactDataEntity);
        return entity;
    }


    static public BusinessEntity quickInsertBusiness(TestEntityManager testEntityManager, ContactDataEntity contactData){
        EmailEntity emailEntity = createEmailData(contactData);
        testEntityManager.persist(emailEntity);
        PhoneNumberEntity phoneNumberEntity = createPhoneNumberEntity(contactData);
        testEntityManager.persist(phoneNumberEntity);

        BusinessEntity businessEntity = createBusinessEntity(contactData);
        return testEntityManager.persist(businessEntity);
    }

    static public ServiceUserEntity quickInsertServiceUserEntity(TestEntityManager testEntityManager, ContactDataEntity contactData){
        EmailEntity emailEntity = createEmailData(contactData);
        testEntityManager.persist(emailEntity);
        PhoneNumberEntity phoneNumberEntity = createPhoneNumberEntity(contactData);
        testEntityManager.persist(phoneNumberEntity);

        ServiceUserEntity serviceUserEntity = createServiceUserEntity(contactData);
        return testEntityManager.persist(serviceUserEntity);
    }

    static public CyclicalServiceEntity quickInsertCyclicalServiceEntity(TestEntityManager testEntityManager){
        ContactDataEntity contactData = quickInsertContactData(testEntityManager);
        CyclicalServiceEntity cyclicalServiceEntity = createCyclicalServiceEntity(false,quickInsertServiceUserEntity(testEntityManager,contactData),quickInsertBusiness(testEntityManager,contactData));
        cyclicalServiceEntity = testEntityManager.persist(cyclicalServiceEntity);
        return cyclicalServiceEntity;
    }

    private static ContactDataEntity quickInsertContactData(TestEntityManager testEntityManager) {
        return testEntityManager.persist(new ContactDataEntity());
    }

    static public CertificateEntity quickInsertCertificateEntity(TestEntityManager testEntityManager, LocalDateTime validTo,boolean mostRecent, boolean renewalMessageSent,CyclicalServiceEntity cyclicalServiceEntity){
        CyclicalServiceEntity cse;
        if (cyclicalServiceEntity == null)
            cse = quickInsertCyclicalServiceEntity(testEntityManager);
        else
            cse = cyclicalServiceEntity;
        CertificateEntity certificateEntity = createCertificateEntity(cse,validTo,mostRecent,renewalMessageSent);
        return testEntityManager.persist(certificateEntity);
    }
    static public CertificateEntity quickRenewCertificate(TestEntityManager testEntityManager, CertificateEntity oldCert,LocalDateTime validTo,boolean mostRecent, boolean renewalMessageSent){
        oldCert.setMostRecent(false);
        testEntityManager.persist(oldCert);


        return quickInsertCertificateEntity(testEntityManager,validTo,mostRecent,renewalMessageSent,oldCert.getCyclicalServiceEntity());
    }

}
