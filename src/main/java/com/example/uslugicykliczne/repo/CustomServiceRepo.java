package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.StatusEnum;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.StatusChangeEntity;
import com.example.uslugicykliczne.entity.StatusTypeEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

interface CustomServiceRepo{
    List<CyclicalServiceProjection> customFindCyclicalProjectionsWithCancelRequest();

    List<CyclicalServiceProjection> customFindCyclicalProjectionsByParam(int param1,SERVICE_FINDING_MODE serviceFindingMode,String accountUsername);


    Optional<CyclicalServiceEntity> findCyclicalServiceAcDataJoin(Integer id);


    Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID);
    void customUpdateAwaitingRenewal();
    void removeExpiredIgnores();
    CyclicalServiceProjection saveAndReturnProjection(CyclicalServiceEntity cyclicalServiceEntity);

    void setExpiredStatus();
}

@RequiredArgsConstructor
class  CustomServiceRepoImpl implements CustomServiceRepo{

    @PersistenceContext
    private EntityManager entityManager;
    private final StatusChangeRepo statusChangeRepo;

    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjectionsWithCancelRequest() {
        Query query = entityManager.createQuery(
                "select  new com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap, " +
                        "cs.serviceUser.accountDataEntity.username)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                        "where floor(mod(cs.statusBitmap/:status,:status)) = 1 and ce.mostRecent=true "
        );
        query.setParameter("status", StatusEnum.MARKED_FOR_CANCEL.getMaskValue());
        return query.getResultList();
    }



    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjectionsByParam(int param1,SERVICE_FINDING_MODE serviceFindingMode,String accountUsername) {
        Query query = null;
        String userCheck = "";
        if(accountUsername!=null)
            userCheck = " and cs.serviceUser.accountDataEntity.username = :accUsername";
        switch (serviceFindingMode){
            //in next n days
            default -> {
                query = entityManager.createQuery(
                "select  new com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap, " +
                        "cs.serviceUser.accountDataEntity.username)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                        "where ce.mostRecent = true  and ce.validTo<:desiredTime"+userCheck);
                if(param1==-1)
                    query.setParameter("desiredTime", LocalDateTime.now().plusYears(100));
                else
                    query.setParameter("desiredTime", LocalDateTime.now().plusDays(param1));
            }
            case BY_USER_ID -> {
                query = entityManager.createQuery(
                        "select  new com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection(" +
                                "cs.idCyclicalService,cs.oneTime,cs.agreementNumber,cs.description," +
                                "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                                "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap, " +
                                "cs.serviceUser.accountDataEntity.username)" +
                                "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                                "where ce.mostRecent = true  and cs.serviceUser.idServiceUser =:userID "+userCheck);

                query.setParameter("userID", param1);
            }
            case BY_BUSINESS_ID -> {
                query = entityManager.createQuery(
                        "select  new com.example.uslugicykliczne.dataTypes.projections.CyclicalServiceProjection(" +
                                "cs.idCyclicalService,cs.oneTime,cs.agreementNumber,cs.description," +
                                "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                                "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap, " +
                                "cs.serviceUser.accountDataEntity.username)" +
                                "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                                "where ce.mostRecent = true  and cs.business.idBusiness =:businessID "+userCheck);

                query.setParameter("businessID", param1);
            }
        }

        if(accountUsername!=null)
            query.setParameter("accUsername", accountUsername);
        return query.getResultList();

    }


//    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"assignedAccountDataEntity"})
//    @org.springframework.data.jpa.repository.Query("select cse from CyclicalServiceEntity cse where cse.idCyclicalService = :id")
    @Override
    public Optional<CyclicalServiceEntity> findCyclicalServiceAcDataJoin(Integer id) {
        EntityGraph<CyclicalServiceEntity> entityGraph = entityManager.createEntityGraph(CyclicalServiceEntity.class);
        entityGraph.addAttributeNodes("serviceUser");
        Subgraph<CyclicalServiceEntity> sub = entityGraph.addSubgraph("serviceUser");
        sub.addAttributeNodes("accountDataEntity");

        Query query = entityManager.createQuery("select cse from CyclicalServiceEntity cse where cse.id = :id");
        query.setHint("jakarta.persistence.loadgraph", entityGraph);

        query.setParameter("id", id);

        List<CyclicalServiceEntity> list = query.getResultList();

        if(list.size()>0)
            return Optional.of(list.get(0));
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID) {

        EntityGraph<CyclicalServiceEntity> entityGraph = entityManager.createEntityGraph(CyclicalServiceEntity.class);
        entityGraph.addAttributeNodes("serviceUser");
        Subgraph<CyclicalServiceEntity> sub = entityGraph.addSubgraph("serviceUser");
        sub.addAttributeNodes("accountDataEntity");

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.fetchgraph", entityGraph);

        CyclicalServiceEntity cyclicalService = entityManager.find(CyclicalServiceEntity.class, serviceID, properties);

        if (cyclicalService==null)
            return Optional.empty();
        return Optional.of(cyclicalService);
//        Query query = entityManager.createQuery(
//                "select cse from CyclicalServiceEntity cse " +
//                        "left join cse.assignedAccountDataEntity aa " +
//                        "where cse.idCyclicalService=:serviceID "
//        );
//        query.setParameter("serviceID",serviceID);
//
//        var answer = query.getResultList();
//        return (CyclicalServiceEntity) answer.get(0);
    }

    @Override
    @Transactional
    public void customUpdateAwaitingRenewal() {
        Query disableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0");
        disableSafeUpdate.executeUpdate();

        Query query = entityManager.createQuery(
                "   select distinct ce.cyclicalServiceEntity.idCyclicalService from CertificateEntity ce" +
                        "   where ce.validTo<:desiredTime and ce.cyclicalServiceEntity.oneTime=false "
        );
        query.setParameter("desiredTime", LocalDateTime.now().plusDays(60));

        List<Integer> idList =  (List<Integer>) query.getResultList();


        query = entityManager.createQuery(
                "update  CyclicalServiceEntity cse " +
                        "set cse.statusBitmap = 1 " +
                        "where cse.idCyclicalService in (:idList) "+
                        "and cse.statusBitmap = :renewedValue or cse.statusBitmap = :newValue"
        );
        query.setParameter("renewedValue",StatusEnum.RENEWED.getMaskValue());
        query.setParameter("newValue",StatusEnum.NEW.getMaskValue());

        query.setParameter("idList",idList);
        query.executeUpdate();


        Query enableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1");
        enableSafeUpdate.executeUpdate();

        LocalDateTime now = LocalDateTime.now();
        List<StatusChangeEntity> statusChangeEntities = new ArrayList<>();
        for(int id:idList){
            StatusChangeEntity statusChangeEntity = new StatusChangeEntity();
            statusChangeEntity.setChangeDate(now);
            statusChangeEntity.setStatusTypeEntity(entityManager.getReference(StatusTypeEntity.class,StatusEnum.AWAITING_RENEWAL.getMaskValue()));
            statusChangeEntity.setComment("Dokonano automatycznego zmianu stanu na \""+StatusEnum.AWAITING_RENEWAL.getStatusName()+"\"");
            statusChangeEntity.setCyclicalService(entityManager.getReference(CyclicalServiceEntity.class,id));
            statusChangeEntities.add(statusChangeEntity);
        }


        statusChangeRepo.saveAll(statusChangeEntities);


    }

    @Override
    @Transactional
    public void removeExpiredIgnores() {
        Query disableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0");
        disableSafeUpdate.executeUpdate();
        Query query = entityManager.createNativeQuery(
                "update  uslugi_cykliczne.cyclical_service cs " +
                        "set cs.status = cs.status & ~(:targetStatus) , cs.ignore_to = null " +
                        "where (cs.status & (:targetStatus)) != 0  and cs.ignore_to<:today"
        );



        query.setParameter("targetStatus", StatusEnum.IGNORE.getMaskValue());
        query.setParameter("today",LocalDateTime.now());

        query.executeUpdate();

        Query enableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1");
        enableSafeUpdate.executeUpdate();

    }

    @Override
    @Transactional
    public void setExpiredStatus() {
        Query disableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0");
        disableSafeUpdate.executeUpdate();

        Query query = entityManager.createQuery(
                "   select distinct ce.cyclicalServiceEntity.idCyclicalService from CertificateEntity ce" +
                        "   where  ce.cyclicalServiceEntity.oneTime=false  and ce.mostRecent and  ce.validTo<:today "
        );

        query.setParameter("today", LocalDateTime.now());
        List<Integer> idList =  (List<Integer>) query.getResultList();


        query = entityManager.createQuery(
                "update  CyclicalServiceEntity cse " +
                        "set cse.statusBitmap = 2048 " +
                        "where cse.idCyclicalService in (:idList) "
        );
//        query.setParameter("renewedValue",StatusEnum.RENEWED.getMaskValue());
//        query.setParameter("newValue",StatusEnum.NEW.getMaskValue());

        query.setParameter("idList",idList);
        query.executeUpdate();


        Query enableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1");
        enableSafeUpdate.executeUpdate();

        LocalDateTime now = LocalDateTime.now();
        List<StatusChangeEntity> statusChangeEntities = new ArrayList<>();
        for(int id:idList){
            StatusChangeEntity statusChangeEntity = new StatusChangeEntity();
            statusChangeEntity.setChangeDate(now);
            statusChangeEntity.setStatusTypeEntity(entityManager.getReference(StatusTypeEntity.class,StatusEnum.EXPIRED.getMaskValue()));
            statusChangeEntity.setComment("Dokonano automatycznego zmianu stanu na \""+StatusEnum.EXPIRED.getStatusName()+"\"");
            statusChangeEntity.setCyclicalService(entityManager.getReference(CyclicalServiceEntity.class,id));
            statusChangeEntities.add(statusChangeEntity);
        }


        statusChangeRepo.saveAll(statusChangeEntities);

    }


    @Override
    public CyclicalServiceProjection saveAndReturnProjection(CyclicalServiceEntity cyclicalServiceEntity) {
//        Query update = entityManager.createQuery("i")
        return null;
    }

}
