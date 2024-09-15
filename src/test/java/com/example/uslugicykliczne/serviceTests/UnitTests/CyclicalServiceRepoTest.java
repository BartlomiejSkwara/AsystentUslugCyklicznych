//package com.example.uslugicykliczne.serviceTests.UnitTests;
//
//import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.time.LocalDateTime;
//import java.time.Period;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class CyclicalServiceRepoTest {
//    @Autowired
//    CyclicalServiceRepo cyclicalServiceRepo;
//
//    @Autowired
//    TestEntityManager testEntityManager;
//    AutoCloseable autoCloseable;
//    @BeforeEach
//    public void setUp() {
//        autoCloseable  = MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach()
//    public void close() throws Exception{
//        autoCloseable.close();
//    }
//    @Test
//    void findFirstDateBeforeTestSuccess(){
//        int [] years = {5,30,2,21,99};
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
//        for (int year:years) {
//            cyclicalServiceRepo.save(
//                new CyclicalServiceEntity(
//                    "opis",1.2,
//                    localDateTime,
//                        Period.of(year,0,0)
//                )
//            );
//        }
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBefore(localDateTime.plus(Period.of(50,0,0)));
//        assertTrue(cyclicalServiceEntity.isPresent());
//        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//
//    @Test
//    @DisplayName("Find first date before test but the db doesn't hold values before a certain date ")
//    void findFirstDateBeforeTestFailure(){
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
//    }
//
//
//    @Test
//    @DisplayName("Find service with next renewal data when db holds no services")
//    void findServiceWithNextRenewalDateNS(){
//
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findServiceWithNextRenewalDate();
//        assertTrue(cyclicalServiceEntity.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Find service with next renewal data when db holds services")
//    void findServiceWithNextRenewalDateS(){
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
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findServiceWithNextRenewalDate();
//        assertTrue(cyclicalServiceEntity.isPresent());
//        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//
//    @Test
//    @DisplayName("Find next service without the reminder message sent")
//    void findServiceWithoutMessageSentTest(){
//        int [] years = {5,30,2,21,99};
//        boolean [] renewed = {false,true,true,true,true};
//
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(5,0,0));
//        CyclicalServiceEntity nextService;
//        for (int i = 0; i<years.length; i++) {
//            nextService =  new CyclicalServiceEntity(
//                    "opis",1.2,
//                    localDateTime,
//                    Period.of(years[i],0,0)
//            );
//            nextService.setRenewalMessageSent(renewed[i]);
//            cyclicalServiceRepo.save(nextService);
//
//        }
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstServiceWithNoMessageSent();
//        assertTrue(cyclicalServiceEntity.isPresent());
//        assertFalse(cyclicalServiceEntity.get().getRenewalMessageSent());
//        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//    @Test
//    @DisplayName("Find next service without the reminder message sent with renewal  before a certain date")
//    void findServiceWithoutMessageSentBeforeTestSuccess(){
//        int [] years = {5,30,2,21,99};
//        boolean [] renewed = {false,true,false,true,true};
//
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
//        CyclicalServiceEntity nextService;
//        for (int i = 0; i<years.length; i++) {
//            nextService =  new CyclicalServiceEntity(
//                    "opis",1.2,
//                    localDateTime,
//                    Period.of(years[i],0,0)
//            );
//            nextService.setRenewalMessageSent(renewed[i]);
//            cyclicalServiceRepo.save(nextService);
//
//        }
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBeforeWithNoMessageSent(localDateTime.plus(Period.of(50,0,0)));
//        assertTrue(cyclicalServiceEntity.isPresent());
//        assertFalse(cyclicalServiceEntity.get().getRenewalMessageSent());
//        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//
//    @Test
//    @DisplayName("Faile to find next service without the reminder message sent with renewal before a certain date due to lack of services that the user was not notified about ")
//    void findServiceWithoutMessageSentBeforeTestFailure(){
//        int [] years = {5,30,2,21,99};
//        boolean [] renewed = {true,true,true,true,false};
//
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
//        CyclicalServiceEntity nextService;
//        for (int i = 0; i<years.length; i++) {
//            nextService =  new CyclicalServiceEntity(
//                    "opis",1.2,
//                    localDateTime,
//                    Period.of(years[i],0,0)
//            );
//            nextService.setRenewalMessageSent(renewed[i]);
//            cyclicalServiceRepo.save(nextService);
//
//        }
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstDateBeforeWithNoMessageSent(localDateTime.plus(Period.of(0,6,0)));
//        assertTrue(cyclicalServiceEntity.isEmpty());
////        assertFalse(cyclicalServiceEntity.get().getRenewalMessageSent());
////        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//
//    @Test
//    @DisplayName("")
//    void findAllServicesWithoutMessageSentBefore(){
//        int [] years = {5,30,2,21,99};
//        boolean [] renewed = {false,true,false,false,false};
//
//        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
//        LocalDateTime answer = localDateTime.plus( Period.of(2,0,0));
//        CyclicalServiceEntity nextService;
//        for (int i = 0; i<years.length; i++) {
//            nextService =  new CyclicalServiceEntity(
//                    "opis",1.2,
//                    localDateTime,
//                    Period.of(years[i],0,0)
//            );
//            nextService.setRenewalMessageSent(renewed[i]);
//            cyclicalServiceRepo.save(nextService);
//
//        }
//        List<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findAllDatesBeforeWithNoMessageSent(localDateTime.plus(Period.of(29,0,0)));
//        assertFalse(cyclicalServiceEntity.isEmpty());
//        assertEquals(3,cyclicalServiceEntity.size());
////        assertFalse(cyclicalServiceEntity.get().getRenewalMessageSent());
////        assertEquals(answer, cyclicalServiceEntity.get().getNextRenewal());
//    }
//
//
//
//
//
//}
