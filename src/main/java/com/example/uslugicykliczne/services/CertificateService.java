package com.example.uslugicykliczne.services;


import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.dataTypes.ServiceRenewalRecord;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CertificateService {
    private final CertificateRepo certificateRepo;

    public CertificateService(CertificateRepo certificateRepo) {
        this.certificateRepo = certificateRepo;
    }

//    public CertificateEntity createCertificate(CyclicalServiceEntity cyclicalServiceEntity, LocalDateTime cycleStart, LocalDateTime cycleEnd, String cardNumber, String cardType, String certSerialNumber, Optional<String> nameInOrganisation) {
//        CertificateEntity certificateEntity = new CertificateEntity();
//        certificateEntity.setCertificateSerialNumber(certSerialNumber);
//        certificateEntity.setCyclicalServiceEntity(cyclicalServiceEntity);
//        certificateEntity.setCardNumber(cardNumber);
//        certificateEntity.setCardType(cardType);
//        certificateEntity.setValidTo(cycleEnd);
//        certificateEntity.setValidFrom(cycleStart);
//        certificateEntity.setNameInOrganisation(nameInOrganisation.orElse(null));
//        return certificateEntity;
//    }

    public CertificateEntity insertCertificateCreatedFromCyclicalServiceDTO(CyclicalServiceEntity cyclicalServiceEntity,CyclicalServiceDto cyclicalServiceDto) {
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setCertificateSerialNumber(cyclicalServiceDto.getCertSerialNumber());
        certificateEntity.setCyclicalServiceEntity(cyclicalServiceEntity);
        certificateEntity.setCardNumber(cyclicalServiceDto.getCardNumber());
        certificateEntity.setCardType(cyclicalServiceDto.getCardType());
        certificateEntity.setValidTo(cyclicalServiceDto.getCycleEnd());
        certificateEntity.setValidFrom(cyclicalServiceDto.getCycleStart());
        certificateEntity.setNameInOrganisation(cyclicalServiceDto.getNameInOrganisation().orElse(null));
        certificateEntity.setMostRecent(true);
        certificateEntity.setRenewalMessageSent(false);
        return certificateRepo.save(certificateEntity);
    }


    public CertificateEntity insertCertificateCreatedFromRenewalRecord(CyclicalServiceEntity cyclicalServiceEntity, ServiceRenewalRecord serviceRenewalRecord) {
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setCertificateSerialNumber(serviceRenewalRecord.certSerialNumber());
        certificateEntity.setCyclicalServiceEntity(cyclicalServiceEntity);
        certificateEntity.setCardNumber(serviceRenewalRecord.cardNumber());
        certificateEntity.setCardType(serviceRenewalRecord.cardType());
        certificateEntity.setValidTo(serviceRenewalRecord.cycleEnd());
        certificateEntity.setValidFrom(serviceRenewalRecord.cycleStart());
        certificateEntity.setNameInOrganisation(serviceRenewalRecord.nameInOrganisation().orElse(null));
        certificateEntity.setMostRecent(true);
        certificateEntity.setRenewalMessageSent(false);
        return certificateRepo.save(certificateEntity);
    }
}
