package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.services.CertificateService;

import java.time.LocalDateTime;

public class TestUtility {



    static public CertificateEntity createCertificateEntity(CyclicalServiceEntity cyclicalServiceEntity,LocalDateTime validTo, boolean renewed, boolean renewalMessageSent) {
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setCertificateSerialNumber("123");
        certificateEntity.setCyclicalServiceEntity(cyclicalServiceEntity);
        certificateEntity.setCardNumber("123");
        certificateEntity.setCardType("physical");
        certificateEntity.setValidTo(validTo);
        certificateEntity.setValidFrom(LocalDateTime.now());
        certificateEntity.setNameInOrganisation("CEO");
        certificateEntity.setRenewed(renewed);
        certificateEntity.setRenewalMessageSent(renewalMessageSent);
        return certificateEntity;
    }

    static public CyclicalServiceEntity createCyclicalServiceEntity(boolean oneTime, ServiceUserEntity serviceUser, BusinessEntity businessEntity){
        CyclicalServiceEntity cyclicalServiceEntity = new CyclicalServiceEntity();
        cyclicalServiceEntity.setServiceUser(serviceUser);
        cyclicalServiceEntity.setBusiness(businessEntity);
        cyclicalServiceEntity.setDescription("service desc");
        cyclicalServiceEntity.setPrice(100);
        cyclicalServiceEntity.setOneTime(oneTime);
        cyclicalServiceEntity.setAgreementNumber("1234");
        return cyclicalServiceEntity;
    }

}
