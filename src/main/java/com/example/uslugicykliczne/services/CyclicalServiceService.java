package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceRenewalRecord;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.repo.BusinessRepo;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import jakarta.persistence.EntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CyclicalServiceService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final ServiceUserRepo serviceUserRepo;
    private final BusinessRepo businessRepo;
    private final CertificateService certificateService;
    private final EntityManager entityManager;
    private final CertificateRepo certificateRepo;
//    private final SchedulingService schedulingService;

    public CyclicalServiceService(CyclicalServiceRepo cyclicalServiceRepo, ServiceUserRepo serviceUserRepo, BusinessRepo businessRepo, CertificateService certificateService, EntityManager entityManager, CertificateRepo certificateRepo) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.serviceUserRepo = serviceUserRepo;
        this.businessRepo = businessRepo;
        this.certificateService = certificateService;
        this.entityManager = entityManager;
        this.certificateRepo = certificateRepo;
    }

    public ResponseEntity<String> renewCyclicalService(ServiceRenewalRecord serviceRenewalRecord, Integer id){
        Optional<CertificateEntity> certificateEntityOptional = certificateRepo.findCertificateWithNotRenewedCertBy(id);
        if(certificateEntityOptional.isEmpty())
            return ResponseEntity.badRequest().body("Can't renew nonexistent service");
        CertificateEntity certificateEntity = certificateEntityOptional.get();

        if(certificateEntity.getCyclicalServiceEntity().isOneTime())
            return ResponseEntity.badRequest().body("Can't renew one time service");

//        if(cyclicalServiceEntity.getCertificates().size()>1)
//            throw  new RuntimeException("UWAGA UWAGA UWAGA : BŁĄD NA MIARĘ ... IDK JAKĄ ALE BARDZO DUŻĄ POINFORMUJ MNIE PROSZĘ O 'PROBLEMACH Z FETCHOWANIEM CERTYFIKATÓW I BŁĘDAMI Z ODNOWIENIEM'");
//
//        if (cyclicalServiceEntity.getCertificates().size()<=0)
//            throw new RuntimeException("Nie wiem jak do tego doprowadziłeś ale .... gratulacje XD ");
        if(certificateEntity.isRenewalMessageSent()){
            //schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity.get());

        } else {
            //schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity.get());
        }
        certificateEntity.setRenewed(true);
        certificateEntity.setRenewalMessageSent(true);

        certificateService.insertCertificateCreatedFromRenewalRecord(certificateEntity.getCyclicalServiceEntity(), serviceRenewalRecord);

        return ResponseEntity.ok().body("The task was successfully renewed");
    }

    public ResponseEntity<String> insertNewCyclicalServiceEntity(CyclicalServiceDto cyclicalServiceDto){
        Optional<ServiceUserEntity> serviceUserEntityOptional = serviceUserRepo.findById(cyclicalServiceDto.getServiceUserId());
        Optional<BusinessEntity> businessEntityOptional = businessRepo.findById(cyclicalServiceDto.getBusinessId());

        if(businessEntityOptional.isPresent() && serviceUserEntityOptional.isPresent()){
            CyclicalServiceEntity insertedEntity = cyclicalServiceRepo.save(createCyclicalServiceDTOFromEntity(new CyclicalServiceEntity(),cyclicalServiceDto,serviceUserEntityOptional.get(),businessEntityOptional.get()));
            certificateService.insertCertificateCreatedFromCyclicalServiceDTO(insertedEntity,cyclicalServiceDto);
            //cyclicalServiceEntity.setCertificates(dto.getRenewalPeriod());
            //schedulingService.trySchedulingReminderWhenInserted(insertedEntity);
            return ResponseEntity.ok("Successfully added the cyclical service");
        }

        StringBuilder stringBuilder = new StringBuilder("Entities :");
        stringBuilder.append((serviceUserEntityOptional.isEmpty())?"[service user]":"");
        stringBuilder.append((businessEntityOptional.isEmpty())?"[business]":"");
        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());
    }

    private CyclicalServiceEntity createCyclicalServiceDTOFromEntity(CyclicalServiceEntity cyclicalServiceEntity, CyclicalServiceDto dto, ServiceUserEntity serviceUserEntity, BusinessEntity businessEntity){
        cyclicalServiceEntity.setServiceUser(serviceUserEntity);
        cyclicalServiceEntity.setBusiness(businessEntity);
        cyclicalServiceEntity.setDescription(dto.getDescription());
        cyclicalServiceEntity.setPrice(dto.getPrice());
        cyclicalServiceEntity.setOneTime(dto.getOneTime());
        cyclicalServiceEntity.setAgreementNumber(dto.getAgreementNumber());

        return cyclicalServiceEntity;
    }

    public List<CyclicalServiceProjection> getAllFromNextNDays(int nDays) {
        return cyclicalServiceRepo.customFindCyclicalProjectionsInNextNDays(nDays);
    }

    public List<CyclicalServiceProjection> getAllByUserId(Integer userID) {
        return cyclicalServiceRepo.customfindCyclicalProjectionsByUserId(userID);
    }


//    public ResponseEntity<String> updateCyclicalServiceEntity(Integer id, CyclicalServiceDto cyclicalServiceDto){
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findById(id);
//        if (cyclicalServiceEntity.isEmpty())
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent cyclical service !!!");
//
//
//        Optional<CustomerEntity> customerEntity = customerRepo.findById(cyclicalServiceDto.getCustomerId());
//        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(cyclicalServiceDto.getDysponentId());
//
//        if(customerEntity.isPresent() && dysponentEntity.isPresent()){
//            CyclicalServiceEntity cyclicalServiceEntity1 = cyclicalServiceRepo.save(convertCyclicalServiceDTOtoEntity(cyclicalServiceEntity.get(),cyclicalServiceDto,customerEntity.get(),dysponentEntity.get()));
//            schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity1);
//            return ResponseEntity.ok("Successfully updated the cyclical service");
//        }
//
//        StringBuilder stringBuilder = new StringBuilder("Entities :");
//        stringBuilder.append((customerEntity.isEmpty())?"[customer]":"");
//        stringBuilder.append((dysponentEntity.isEmpty())?"[dysponent]":"");
//        stringBuilder.append("with the provided ID(s) could not be found.");
//        return  ResponseEntity.badRequest().body(stringBuilder.toString());
//
//
//
//    }
//
//
}
