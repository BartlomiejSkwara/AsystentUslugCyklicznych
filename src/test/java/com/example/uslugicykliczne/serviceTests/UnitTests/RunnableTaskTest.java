package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.EmailService;
import com.example.uslugicykliczne.services.SchedulingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RunnableTaskTest {


    @Autowired
    CyclicalServiceRepo cyclicalServiceRepo;
    @Autowired
    CertificateRepo certificateRepo;
    @Mock
    SchedulingService schedulingService;
    @Mock
    EmailService emailService;

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
    void testRunMethod(){
//        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
//        CyclicalServiceEntity cs2 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
//        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusYears(5), false, false);
//        CertificateEntity ce2 = TestUtilityService.createCertificateEntity(cs2,LocalDateTime.now().plusYears(5), false, false);
//        cs1.setIdCyclicalService(1);
//        cs2.setIdCyclicalService(2);
//        ce1.setIdCertificate(1);
//        ce2.setIdCertificate(2);
//        cyclicalServiceRepo.save(cs1);
//        cyclicalServiceRepo.save(cs2);
//        certificateRepo.save(ce1);
//        certificateRepo.save(ce2);
//        cyclicalServiceEntity1.setId(0);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, cyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().minusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, cyclicalServiceRepo, schedulingService, emailService);
//        LocalDateTime answerDate = cyclicalServiceEntity2.getNextRenewal().plus(cyclicalServiceEntity2.getRenewalPeriod());
//
//        cyclicalServiceRepo.save(cyclicalServiceEntity1);
//        cyclicalServiceRepo.save(cyclicalServiceEntity2);
//
//
//        RunnableTask runnableTask = new RunnableTask(cyclicalServiceEntity2,cyclicalServiceRepo,schedulingService, emailService);
//        runnableTask.run();
//
//
//        Optional<CyclicalServiceEntity> change = cyclicalServiceRepo.findById(1);
//
//        assertTrue(change.isPresent());
//        assertEquals(change.get().getNextRenewal(),answerDate);

    }
}
