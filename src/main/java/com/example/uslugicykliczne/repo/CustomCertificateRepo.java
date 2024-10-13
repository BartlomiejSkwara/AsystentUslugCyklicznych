package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CertificateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public interface CustomCertificateRepo {
    Optional<CertificateEntity> findCertificateWithNotRenewedCertBy(int serviceId);
    Optional<CertificateEntity> findFirstCertificateWithNoRenewalAndMessageSent();
}


class  CustomCertificateRepoImpl implements CustomCertificateRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CertificateEntity> findCertificateWithNotRenewedCertBy(int serviceId) {
        Query query = entityManager.createQuery(
                "select ce from CertificateEntity ce where ce.cyclicalServiceEntity.idCyclicalService = :serviceId and ce.renewed = false"
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
    public Optional<CertificateEntity> findFirstCertificateWithNoRenewalAndMessageSent() {
        Query query = entityManager.createQuery(
                "select ce from CertificateEntity ce join CyclicalServiceEntity cs on ce.cyclicalServiceEntity = cs " +
                        "where ce.renewalMessageSent = false and ce.renewed = false " +
                        "order by ce.validTo asc limit 1"
        );
        List<CertificateEntity> list = query.getResultList();
        if (list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }
}
