package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CertificateEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CertificateRepo extends ListCrudRepository<CertificateEntity,Integer> {
}
