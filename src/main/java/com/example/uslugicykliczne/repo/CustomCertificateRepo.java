package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CertificateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public interface CustomCertificateRepo {
    Optional<CertificateEntity> findCertificateWithNotRenewedCertBy(int nDays);
}


class  CustomCertificateRepoImpl implements CustomCertificateRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CertificateEntity> findCertificateWithNotRenewedCertBy(int nDays) {
        Query query = entityManager.createQuery(
                "select ce from CertificateEntity ce where ce.cyclicalServiceEntity.idCyclicalService = :serviceId and ce.renewed = false"
        );
        query.setParameter("serviceId", nDays);
        List<CertificateEntity> list = query.getResultList();
        if (list.size()==0)
            return Optional.empty();
        else if (list.size() == 1)
            return Optional.of(list.get(0));

        throw new RuntimeException("UWAGA wystąpił mega wyjątek z certyfikatami proszę powiadom mnie o nim :< ");
    }
}
