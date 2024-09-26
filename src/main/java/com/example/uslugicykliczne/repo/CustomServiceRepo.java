package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

interface CustomServiceRepo{
    List<CyclicalServiceProjection> customFindCyclicalProjectionsInNextNDays(int nDays);
    Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID);

}
class  CustomServiceRepoImpl implements CustomServiceRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjectionsInNextNDays(int nDays) {
        Query query = entityManager.createQuery(
                "select  new com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.price,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                        "where ce.renewed = false  and ce.validTo<:desiredTime"
        );
        query.setParameter("desiredTime", LocalDateTime.now().plusDays(nDays));
        return query.getResultList();
    }

    @Override
    public Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID) {


        EntityGraph entityGraph = entityManager.createEntityGraph(CyclicalServiceEntity.class);
        entityGraph.addAttributeNodes("statusBitmap", "assignedAccountDataEntity");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
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
}
