package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CertificateRepoTest {
    @Autowired
    CertificateRepo certificateRepo;

    @Autowired
    TestEntityManager testEntityManager;
    AutoCloseable autoCloseable;
    @BeforeEach
    public void setUp() {
        autoCloseable  = MockitoAnnotations.openMocks(this);
    }

    @AfterEach()
    public void close() throws Exception{
        autoCloseable.close();
    }
    @Test
    void findFirstCertificateWithNoRenewalAndMessageSent(){
        TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(1),true,true,null);
        Optional<CertificateEntity> answer;
        answer =  certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        assertTrue(answer.isEmpty());

        TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(1),true,false,null);
        answer =  certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        assertTrue(answer.isEmpty());

        TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(1),false,true,null);
        answer =  certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        assertTrue(answer.isEmpty());

        CertificateEntity newCert = TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(10),false,false,null);
        answer =  certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        assertTrue(answer.isPresent());
        assertEquals(newCert,answer.get());

        newCert = TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(9),false,false,null);
        answer =  certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        assertTrue(answer.isPresent());
        assertEquals(newCert,answer.get());

//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBefore(localDateTime.plus(Period.of(50,0,0)));
//        assertTrue(cyclicalServiceEntity.isPresent());
//        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
    }


    @Test
    void findFirstDateBeforeTestFailure(){


        CertificateEntity newCert = TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(11),true,true,null);
        Integer serviceId = newCert.getCyclicalServiceEntity().getIdCyclicalService();
        Optional<CertificateEntity> answer;
        answer =  certificateRepo.findCertificateWithNotRenewedCertBy(serviceId);
        assertTrue(answer.isEmpty());

        newCert = TestUtilityService.quickInsertCertificateEntity(testEntityManager,LocalDateTime.now().plusYears(11),true,false,null);
        serviceId = newCert.getCyclicalServiceEntity().getIdCyclicalService();
        answer =  certificateRepo.findCertificateWithNotRenewedCertBy(serviceId);
        assertTrue(answer.isEmpty());

        newCert = TestUtilityService.quickRenewCertificate(testEntityManager,newCert,LocalDateTime.now().plusYears(12),false,false);
        answer = certificateRepo.findCertificateWithNotRenewedCertBy(serviceId);
        assertTrue(answer.isPresent());
        assertEquals(newCert,answer.get());

//        int [] years = {5,30,2,21,99};
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
//        for (int year:years) {
//            cyclicalServiceRepo.save(
//                    new CyclicalServiceEntity(
//                            "opis",1.2,
//                            localDateTime,
//                            Period.of(year,0,0)
//                    )
//            );
//        }
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBefore(localDateTime.plus(Period.of(0,6,0)));
//        assertTrue(cyclicalServiceEntity.isEmpty());
    }




}
