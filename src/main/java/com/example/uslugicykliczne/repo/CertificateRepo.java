package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepo extends ListCrudRepository<CertificateEntity,Integer>, CustomCertificateRepo {


}
