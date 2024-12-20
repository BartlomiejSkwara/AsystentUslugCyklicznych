package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.dataTypes.projections.CertificateProjectionRecord;
import com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceRenewalRecord;
import com.example.uslugicykliczne.dataTypes.StatusEnum;
import com.example.uslugicykliczne.dataTypes.projections.StatusChangeRecordProjection;
import com.example.uslugicykliczne.entity.*;
import com.example.uslugicykliczne.repo.*;
import com.example.uslugicykliczne.security.CustomUserDetails;
import com.example.uslugicykliczne.utility.StatusUtility;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CyclicalServiceService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final ServiceUserRepo serviceUserRepo;
    private final BusinessRepo businessRepo;
    private final CertificateService certificateService;
    private final EntityManager entityManager;
    private final CertificateRepo certificateRepo;
//    private final SchedulingService schedulingService;
    private final ServiceStatusHistoryService serviceStatusHistoryService;
    private final StatusChangeRepo statusChangeRepo;
    private final AccountDataRepo accountDataRepo;


    public ResponseEntity<String> renewCyclicalService(ServiceRenewalRecord serviceRenewalRecord, Integer serviceId){

        switch (serviceRenewalRecord.certificateLengthInYears()){
            case 1,2,3:
                break;
            default:
                return  ResponseEntity.badRequest().body("Podano błędny okres trwania certyfikatu");
        }

        Optional<CertificateEntity> oldCertificateEntityOptional = certificateRepo.findMostRecentCertificate(serviceId);
        if(oldCertificateEntityOptional.isEmpty())
            return ResponseEntity.badRequest().body("Nie można odnowić nie istniejącej usługi");
        CertificateEntity certificateEntity = oldCertificateEntityOptional.get();

        if(certificateEntity.getCyclicalServiceEntity().isOneTime())
            return ResponseEntity.badRequest().body("Nie można odnowić usługi jednorazowej");


        if(certificateEntity.isRenewalMessageSent()){
//            schedulingService.trySchedulingReminderWhenInserted(certificateEntity,certificateEntity.getCyclicalServiceEntity());
//Todo może w przyszłości przywrócę remindery ale na razie zostają zablokowane
        }
        //co z tym zrobić? wywala forbidden 403
//        else {
//            schedulingService.trySchedulingReminderWhenUpdated(certificateEntity,certificateEntity.getCyclicalServiceEntity());
//        }
        certificateEntity.setMostRecent(false);
        certificateEntity.setRenewalMessageSent(true);

        CyclicalServiceEntity cyclicalService = certificateEntity.getCyclicalServiceEntity();

        changeServiceStatus(cyclicalService, StatusEnum.RENEWED.getMaskValue());
        serviceStatusHistoryService.addNewStatusHistoryRecord(null,StatusEnum.RENEWED,"Usługa została odnowiona",serviceId);
        certificateService.insertCertificateCreatedFromRenewalRecord(cyclicalService, serviceRenewalRecord,certificateEntity.getValidTo(),certificateEntity.getCardType());

        return ResponseEntity.ok().body("Z powodzeniem odnowiono usługę");
    }

    public ResponseEntity<String> insertNewCyclicalServiceEntity(@NotNull CyclicalServiceDto cyclicalServiceDto){

        switch (cyclicalServiceDto.getCertificateLengthInYears()){
            case 1,2,3:
                break;
            default:
                return  ResponseEntity.badRequest().body("Podano błędny okres trwania certyfikatu");
        }

//        switch (cyclicalServiceDto.getSignatureType()){
//            case 0,1,2,3:
//                break;
//            default:
//                return  ResponseEntity.badRequest().body("Wybrano błędny typ sygnatury");
//        }


        Optional<ServiceUserEntity> serviceUserEntityOptional = serviceUserRepo.findById(cyclicalServiceDto.getServiceUserId());
        Optional<BusinessEntity> businessEntityOptional = businessRepo.findById(cyclicalServiceDto.getBusinessId());

        if(businessEntityOptional.isPresent() && serviceUserEntityOptional.isPresent()){

            CyclicalServiceEntity insertedEntity = createCyclicalServiceEntityFromDTO(new CyclicalServiceEntity(),cyclicalServiceDto,serviceUserEntityOptional.get(),businessEntityOptional.get());
            insertedEntity.setStatusBitmap(StatusEnum.NEW.getMaskValue());
            AccountDataEntity accountDataEntity = null;

//            Optional<AccountDataEntity> optionalAccountDataEntity = accountDataRepo.findById(cyclicalServiceDto.getRelatedAccountId());
//            if(optionalAccountDataEntity.isEmpty())
//                return  ResponseEntity.internalServerError().body("Can't link cyclical service to nonexistant account");
//            accountDataEntity = optionalAccountDataEntity.get();


//            insertedEntity.setAssignedAccountDataEntity(accountDataEntity);
            insertedEntity = cyclicalServiceRepo.save(insertedEntity);
            CertificateEntity certificateEntity = certificateService.insertCertificateCreatedFromCyclicalServiceDTO(insertedEntity,cyclicalServiceDto);
            //cyclicalServiceEntity.setCertificates(dto.getRenewalPeriod());
            //schedulingService.trySchedulingReminderWhenInserted(insertedEntity);
            if(certificateEntity!=null){
//                schedulingService.trySchedulingReminderWhenInserted(certificateEntity,insertedEntity);
                return ResponseEntity.ok("Successfully added the cyclical service");

            }
            return  ResponseEntity.internalServerError().body("Couldn't create certificate");
        }

        StringBuilder stringBuilder = new StringBuilder("Entities :");
        stringBuilder.append((serviceUserEntityOptional.isEmpty())?"[service user]":"");
        stringBuilder.append((businessEntityOptional.isEmpty())?"[business]":"");
        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());
    }

    public ResponseEntity<String> updateCyclicalServiceEntity(Integer id, CyclicalServiceDto cyclicalServiceDto){
        Optional<CyclicalServiceEntity> cyclicalServiceEntityOptional = cyclicalServiceRepo.findById(id);
        if(cyclicalServiceEntityOptional.isEmpty())
            return  ResponseEntity.internalServerError().body("Nie można zaktualizować danych nie istniejącej usługi");
        CyclicalServiceEntity updatedEntity = cyclicalServiceEntityOptional.get();

        Optional<ServiceUserEntity> serviceUserEntityOptional = serviceUserRepo.findById(cyclicalServiceDto.getServiceUserId());
        Optional<BusinessEntity> businessEntityOptional = businessRepo.findById(cyclicalServiceDto.getBusinessId());

        if(businessEntityOptional.isPresent() && serviceUserEntityOptional.isPresent()){

            updatedEntity = createCyclicalServiceEntityFromDTO(updatedEntity,cyclicalServiceDto,serviceUserEntityOptional.get(),businessEntityOptional.get());
            AccountDataEntity accountDataEntity = null;

//            Optional<AccountDataEntity> optionalAccountDataEntity = accountDataRepo.findById(cyclicalServiceDto.getRelatedAccountId());
//            if(optionalAccountDataEntity.isEmpty())
//                return  ResponseEntity.internalServerError().body("Can't link cyclical service to nonexistant account");
//            accountDataEntity = optionalAccountDataEntity.get();
//
//
//            updatedEntity.setAssignedAccountDataEntity(accountDataEntity);
            updatedEntity = cyclicalServiceRepo.save(updatedEntity);

            Optional<CertificateEntity> certificateEntity = certificateRepo.findMostRecentCertificate(id);
            if(certificateEntity.isPresent()){
                CertificateEntity editedCert = certificateEntity.get();
                editedCert.setCardNumber(cyclicalServiceDto.getCardNumber());
                editedCert.setCardType(cyclicalServiceDto.getCardType());

                editedCert.setValidTo(editedCert.getValidFrom().plusYears(cyclicalServiceDto.getCertificateLengthInYears()));
                editedCert.setCertificateSerialNumber(cyclicalServiceDto.getCertSerialNumber());
                if(cyclicalServiceDto.getNameInOrganisation().isPresent())
                    editedCert.setNameInOrganisation(cyclicalServiceDto.getNameInOrganisation().get());

                certificateRepo.save(editedCert);
            }
            //cyclicalServiceEntity.setCertificates(dto.getRenewalPeriod());
            //schedulingService.trySchedulingReminderWhenInserted(insertedEntity);
//            if(certificateEntity!=null){
//                schedulingService.trySchedulingReminderWhenInserted(certificateEntity,insertedEntity);
                return ResponseEntity.ok("Z powodzeniem zaktualizowano dane usługi cyklicznej");
//
//            }
//            return  ResponseEntity.internalServerError().body("Couldn't create certificate");
        }

        StringBuilder stringBuilder = new StringBuilder("Entities :");
        stringBuilder.append((serviceUserEntityOptional.isEmpty())?"[service user]":"");
        stringBuilder.append((businessEntityOptional.isEmpty())?"[business]":"");
        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());



    }



    private CyclicalServiceEntity createCyclicalServiceEntityFromDTO(CyclicalServiceEntity cyclicalServiceEntity, CyclicalServiceDto dto, ServiceUserEntity serviceUserEntity, BusinessEntity businessEntity){
        cyclicalServiceEntity.setServiceUser(serviceUserEntity);
        cyclicalServiceEntity.setBusiness(businessEntity);
        cyclicalServiceEntity.setDescription(dto.getDescription());
        cyclicalServiceEntity.setOneTime(dto.getOneTime());
        cyclicalServiceEntity.setAgreementNumber(dto.getAgreementNumber());
//        cyclicalServiceEntity.setSignatureType(dto.getSignatureType());

        return cyclicalServiceEntity;
    }

    public ResponseEntity<?> changeServiceStatusAndUpdateDB(Integer id,Integer requestedStatusChange, String statusChangeComment) {
        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.findById(id);
        if(optionalCyclicalServiceEntity.isEmpty())
            return ResponseEntity.badRequest().body("Nie można zmienić statusu nie istniejącej usługi");

        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();
        changeServiceStatus(cyclicalService,requestedStatusChange);

        serviceStatusHistoryService.addNewStatusHistoryRecord(null,requestedStatusChange,statusChangeComment,cyclicalService.getIdCyclicalService());
        cyclicalServiceRepo.save(cyclicalService);
        return ResponseEntity.ok(cyclicalService.getStatusBitmap());
    }

    public void changeServiceStatus(CyclicalServiceEntity cyclicalService,Integer requestedStatusChange) {
        int statusBitmap = cyclicalService.getStatusBitmap();
        if(StatusUtility.hasStatus(statusBitmap,StatusEnum.RENEWED)||StatusUtility.hasStatus(statusBitmap,StatusEnum.NEW)){
            statusBitmap = 0;
        }


        if (requestedStatusChange.equals(StatusEnum.RENEWED.getMaskValue())||
//                requestedStatusChange.equals(StatusEnum.AWAITING_RENEWAL.getMaskValue())||
                requestedStatusChange.equals(StatusEnum.CANCELED.getMaskValue())||
                requestedStatusChange.equals(StatusEnum.RENEWED_ELSEWHERE.getMaskValue()))
            statusBitmap = requestedStatusChange;
        else
            statusBitmap = StatusUtility.addStatus(statusBitmap,requestedStatusChange);

        cyclicalService.setStatusBitmap(statusBitmap);
    }


    public ResponseEntity<?> cancelRequest(Integer serviceId,String statusChangeComment) {

        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.findCyclicalServiceAcDataJoin(serviceId);
        if(optionalCyclicalServiceEntity.isEmpty())
            return ResponseEntity.badRequest().body("Nie można odnowić usługi która nie istnieje !!!");

        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();


        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(cyclicalService.getServiceUser().getAccountDataEntity().getUsername()))
            return new ResponseEntity<>("Nie modyfikuj cudzych zasobów !!!", HttpStatus.FORBIDDEN);

        changeServiceStatus(cyclicalService,StatusEnum.MARKED_FOR_CANCEL.getMaskValue());

        serviceStatusHistoryService.addNewStatusHistoryRecord(null,StatusEnum.MARKED_FOR_CANCEL.getMaskValue(),statusChangeComment,cyclicalService.getIdCyclicalService());
        cyclicalServiceRepo.save(cyclicalService);

        return ResponseEntity.ok(cyclicalService.getStatusBitmap());
    }

    public ResponseEntity<?> requestRenewal(Integer serviceId, String statusChangeComment){
        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.customFindNameOfAccountAssignedToService(serviceId);
        if(optionalCyclicalServiceEntity.isEmpty())
            return ResponseEntity.badRequest().body("Nie można odnowić usługi która nie istnieje !!!");
        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(cyclicalService.getServiceUser().getAccountDataEntity().getUsername()))
            return new ResponseEntity<>("Nie modyfikuj cudzych zasobów !!!", HttpStatus.FORBIDDEN);


        if (!StatusUtility.hasStatus(cyclicalService.getStatusBitmap(),StatusEnum.RENEWED)&&!StatusUtility.hasStatus(cyclicalService.getStatusBitmap(),StatusEnum.NEW)){
            return ResponseEntity.badRequest().body("Można prosić o odnowienie tylko gdy usługa jest Nowa lub została Odnowiona ");
        }
        changeServiceStatus(cyclicalService,StatusEnum.AWAITING_RENEWAL.getMaskValue());
        serviceStatusHistoryService.addNewStatusHistoryRecord(null,StatusEnum.AWAITING_RENEWAL.getMaskValue(),statusChangeComment,cyclicalService.getIdCyclicalService());
//        CyclicalServiceProjection cycSerProjection =
        cyclicalServiceRepo.save(cyclicalService);

        return ResponseEntity.ok(cyclicalService.getStatusBitmap());
    }


    public List<CyclicalServiceProjection> getAllByFindingModeExcluding(List<StatusEnum> excludedStatuses, int nDays,SERVICE_FINDING_MODE serviceFindingMode){
        List<CyclicalServiceProjection> found = getAllByFindingMode(nDays, SERVICE_FINDING_MODE.IN_NEXT_N_DAYS);
        for (StatusEnum statusEnum : excludedStatuses) {
            found.removeIf(proj-> StatusUtility.hasStatus(proj.getStatusBitmask(),statusEnum));
        }
        return found;
    }
    public List<CyclicalServiceProjection> getAllByFindingModeIncluding(List<StatusEnum> includedStatuses, int nDays,SERVICE_FINDING_MODE serviceFindingMode){
        List<CyclicalServiceProjection> found = getAllByFindingMode(nDays, SERVICE_FINDING_MODE.IN_NEXT_N_DAYS);
        for (StatusEnum statusEnum : includedStatuses) {
            found.removeIf(proj-> !StatusUtility.hasStatus(proj.getStatusBitmask(),statusEnum));
        }
        return found;
    }

    public List<CyclicalServiceProjection> getAllByFindingMode(int nDays,SERVICE_FINDING_MODE serviceFindingMode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = ((CustomUserDetails)authentication.getPrincipal());

        if (userDetails.getRole().equals("ROLE_admin")||userDetails.getRole().equals("ROLE_editor")){
            return cyclicalServiceRepo.customFindCyclicalProjectionsByParam(nDays,serviceFindingMode,null);
        }

        return  cyclicalServiceRepo.customFindCyclicalProjectionsByParam(nDays,serviceFindingMode,authentication.getName());

    }

    @Transactional
    public List<StatusChangeRecordProjection> getStatusChangesRelatedToService(Integer serviceId) {

        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.findCyclicalServiceAcDataJoin(serviceId);
        if (optionalCyclicalServiceEntity.isEmpty())
            return List.of();

        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = ((CustomUserDetails)authentication.getPrincipal());

        if (!userDetails.getRole().equals("ROLE_admin")&&!userDetails.getRole().equals("ROLE_editor")){
            if(!authentication.getName().equals(cyclicalService.getServiceUser().getAccountDataEntity().getUsername()))
                return List.of();
        }


        return statusChangeRepo.findByServiceIdWithChronologicalOrder(serviceId);
    }

    @Transactional
    public List<CertificateProjectionRecord> getCertificatesRelatedToService(Integer serviceId) {

        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.findCyclicalServiceAcDataJoin(serviceId);
        if (optionalCyclicalServiceEntity.isEmpty())
            return List.of();


        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = ((CustomUserDetails)authentication.getPrincipal());

        if (!userDetails.getRole().equals("ROLE_admin")&&!userDetails.getRole().equals("ROLE_editor")){
            if(!authentication.getName().equals(cyclicalService.getServiceUser().getAccountDataEntity().getUsername()))
                return List.of();
        }


        return certificateRepo.findByServiceIdWithChronologicalOrder(serviceId);
    }

    public ResponseEntity<?> ignore(Integer id, Integer days) {
        Optional<CyclicalServiceEntity> optionalCyclicalServiceEntity = cyclicalServiceRepo.findById(id);
        if(optionalCyclicalServiceEntity.isEmpty())
            return ResponseEntity.badRequest().body("Nie można ignorować nie istniejącej usługi");

        CyclicalServiceEntity cyclicalService = optionalCyclicalServiceEntity.get();
        changeServiceStatus(cyclicalService,StatusEnum.IGNORE.getMaskValue());

        Optional<CertificateEntity> certificateEntity = certificateRepo.findMostRecentCertificate(id);
        if (certificateEntity.isEmpty())
            return ResponseEntity.badRequest().body("Error");
        if(certificateEntity.get().getValidTo().isBefore(LocalDateTime.now().plusDays(days))){
            return ResponseEntity.badRequest().body("Nie można ignorować ignorować na okres 7 dni przed okresem wygaśnięcia");

        }


        serviceStatusHistoryService.addNewStatusHistoryRecord(null,StatusEnum.IGNORE,"Zmieniono status na \"Ignorowany\" na kolejne "+days+" dni",id);


        cyclicalService.setIgnoreTo(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusDays(days));
        cyclicalServiceRepo.save(cyclicalService);
        return ResponseEntity.ok(cyclicalService.getStatusBitmap());

    }
}
