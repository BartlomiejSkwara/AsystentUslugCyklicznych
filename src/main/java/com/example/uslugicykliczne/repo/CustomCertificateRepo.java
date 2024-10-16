package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.projections.CertificateProjectionRecord;
import com.example.uslugicykliczne.dataTypes.projections.StatusChangeRecordProjection;
import com.example.uslugicykliczne.entity.CertificateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public interface CustomCertificateRepo {
    Optional<CertificateEntity> findMostRecentCertificate(int serviceId);
    Optional<CertificateEntity> findFirstMostRecentCertificateWithoutMessageSent();
    List<CertificateProjectionRecord> findByServiceIdWithChronologicalOrder(Integer id);

}


class  CustomCertificateRepoImpl implements CustomCertificateRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CertificateEntity> findMostRecentCertificate(int serviceId) {
        Query query = entityManager.createQuery(
                "select ce from CertificateEntity ce where ce.cyclicalServiceEntity.idCyclicalService = :serviceId and ce.mostRecent = true "
        );
        query.setParameter("serviceId", serviceId);
        List<CertificateEntity> list = query.getResultList();
        if (list.size()==0)
            return Optional.empty();
        else if (list.size() == 1)
            return Optional.of(list.get(0));

        throw new RuntimeException("UWAGA wystąpił mega wyjątek z certyfikatami proszę powiadom mnie o nim :< ");
    }
//ORDER BY e.nextRenewal ASC LIMIT 1
    @Override
    public Optional<CertificateEntity> findFirstMostRecentCertificateWithoutMessageSent() {
        Query query = entityManager.createQuery(
                "select ce from CertificateEntity ce join CyclicalServiceEntity cs on ce.cyclicalServiceEntity = cs " +
                        "where ce.renewalMessageSent = false and ce.mostRecent = true " +
                        "order by ce.validTo asc limit 1"
        );
        List<CertificateEntity> list = query.getResultList();
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public List<CertificateProjectionRecord> findByServiceIdWithChronologicalOrder(Integer id) {
        Query query = entityManager.createQuery(
                "select new com.example.uslugicykliczne.dataTypes.projections.CertificateProjectionRecord(ce.idCertificate,ce.certificateSerialNumber,ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation) " +
                        "from  CertificateEntity ce where ce.cyclicalServiceEntity.idCyclicalService=:serId order by ce.validFrom asc "

        );
        query.setParameter("serId",id);

        List<CertificateProjectionRecord> resultList = query.getResultList();

        return resultList;
    }
}
