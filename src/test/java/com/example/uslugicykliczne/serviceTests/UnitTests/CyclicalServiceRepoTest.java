package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CyclicalServiceRepoTest {
    @Autowired
    CyclicalServiceRepo cyclicalServiceRepo;

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
    void findFirstDateBeforeTestSuccess(){
        int [] years = {5,30,2,21,99};
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
        for (int year:years) {
            cyclicalServiceRepo.save(
                new CyclicalServiceEntity(
                    "opis",1.2,
                    localDateTime,
                        Period.of(year,0,0)
                )
            );
        }
        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBefore(localDateTime.plus(Period.of(50,0,0)));
        assertTrue(cyclicalServiceEntity.isPresent());
        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
    }


    @Test
    @DisplayName("Find first date before test but the db doesn't hold values in the correct timeframe ")
    void findFirstDateBeforeTestFailure(){
        int [] years = {5,30,2,21,99};
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
        for (int year:years) {
            cyclicalServiceRepo.save(
                    new CyclicalServiceEntity(
                            "opis",1.2,
                            localDateTime,
                            Period.of(year,0,0)
                    )
            );
        }
        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBefore(localDateTime.plus(Period.of(0,6,0)));
        assertTrue(cyclicalServiceEntity.isEmpty());
    }


    @Test
    @DisplayName("Find service with next renewal data when db holds no services")
    void findServiceWithNextRenewalDateNS(){

        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findServiceWithNextRenewalDate();
        assertTrue(cyclicalServiceEntity.isEmpty());
    }

    @Test
    @DisplayName("Find service with next renewal data when db holds services")
    void findServiceWithNextRenewalDateS(){
        int [] years = {5,30,2,21,99};
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
        for (int year:years) {
            cyclicalServiceRepo.save(
                    new CyclicalServiceEntity(
                            "opis",1.2,
                            localDateTime,
                            Period.of(year,0,0)
                    )
            );
        }
        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findServiceWithNextRenewalDate();
        assertTrue(cyclicalServiceEntity.isPresent());
        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
    }

}
