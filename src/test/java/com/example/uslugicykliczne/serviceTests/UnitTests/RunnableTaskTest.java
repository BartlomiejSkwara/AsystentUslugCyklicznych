package com.example.uslugicykliczne.serviceTests.UnitTests;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.services.SchedulingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest

public class RunnableTaskTest {

    @Autowired
    CyclicalServiceRepo cyclicalServiceRepo;
    @Mock
    SchedulingService schedulingService;

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
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(5),
                Period.of(3,0,0)
        );
        cyclicalServiceEntity1.setId(0);
        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, cyclicalServiceRepo, schedulingService);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, cyclicalServiceRepo, schedulingService);
        LocalDateTime answerDate = cyclicalServiceEntity2.getNextRenewal().plus(cyclicalServiceEntity2.getRenewalPeriod());

        cyclicalServiceRepo.save(cyclicalServiceEntity1);
        cyclicalServiceRepo.save(cyclicalServiceEntity2);


        RunnableTask runnableTask = new RunnableTask(cyclicalServiceEntity2,cyclicalServiceRepo,schedulingService);
        runnableTask.run();


        Optional<CyclicalServiceEntity> change = cyclicalServiceRepo.findById(1);

        assertTrue(change.isPresent());
        assertEquals(change.get().getNextRenewal(),answerDate);

    }
}
