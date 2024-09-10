package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

interface CustomServiceRepo{
    List<CyclicalServiceProjection> customFindCyclicalProjections();
}
class  CustomServiceRepoImpl implements CustomServiceRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjections() {

        Query query = entityManager.createQuery(
                "select new com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.renewalMessageSent,cs.price,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs"
        );
        return query.getResultList();
    }
}
