package com.example.uslugicykliczne.serviceTests.UnitTests;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.services.SchedulingService;
import com.example.uslugicykliczne.utility.TimeUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.scheduling.TaskScheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class ScheduledReminderTest {
    @Mock
    private TaskScheduler mockTaskScheduler;

    @Mock
    private CyclicalServiceRepo mockCyclicalServiceRepo;

    @Mock
    private ScheduledFuture scheduledFutureHelper;

    @Mock
    private RunnableTask runnableTaskHelper;


    @Spy
    @InjectMocks
    private SchedulingService schedulingService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void setUp() {
        autoCloseable  = MockitoAnnotations.openMocks(this);
    }

    @AfterEach()
    public void close() throws Exception{
        autoCloseable.close();
    }
    @Test()
    @DisplayName("Try scheduling reminder (no reminders before)")
    public void testTrySchedulingNewReminderNRB() {
        CyclicalServiceEntity cyclicalServiceEntity = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(1,0,0)
        );
        RunnableTask task = new RunnableTask("Reminder about: "+cyclicalServiceEntity.toString(), mockCyclicalServiceRepo);

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity);

        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task)),
                eq(cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(0)).cancel(false);

        assertEquals(cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }

    @Test()
    @DisplayName("Try scheduling reminder (last reminder executes earlier(do nothing))")
    public void testTrySchedulingNewReminderLREE() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(1,0,0)
        );
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(2,0,0)
        );
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);

        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity2);



        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task1)),
                eq(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler, times(0)).schedule(
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(0)).cancel(false);
        assertEquals( cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }

    @Test()
    @DisplayName("Try scheduling reminder (last reminder executes later (change and cancel))")
    public void testTrySchedulingNewReminderLREL() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(3,0,0)
        );
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().plusYears(1),
                Period.of(2,0,0)
        );
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);

        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity2);



        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task1)),
                eq(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(1)).cancel(false);
        assertEquals(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }


    @Test()
    @DisplayName("Try scheduling reminder (task with renewal in past)")
    public void testTrySchedulingNewReminderTWRIP() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(2),
                Period.of(1,0,0)
        );
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(2),
                Period.of(1,0,0)
        );
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);


        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity2);


        verify(scheduledFutureHelper,times(0)).cancel(false);
        verify(mockTaskScheduler, times(0)).schedule(
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }


    @Test()
    @DisplayName("Try scheduling reminder (task with renewal date of today)")
    public void testTrySchedulingNewReminderTWRDOT() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(2),
                Period.of(1,0,0)
        );
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(1).plusMinutes(5),
                Period.of(1,0,0)
        );
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);

        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity2);






        verify(scheduledFutureHelper,times(0)).cancel(false);
        verify(mockTaskScheduler, times(0)).schedule(
                //poprawka
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }

    @Test
    @DisplayName("Try scheduling reminder update when the updated entity is not the last registered one")
    public void testTrySchedulingUpdatedReminderWTUEINTLRO(){
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(0);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        verify(schedulingService, times(1)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);
    }



    @Test()
    @DisplayName("Try scheduling reminder update (task with renewal in past)")
    public void testTrySchedulingUpdatedReminderTWRIP() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(1);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);


        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        verify(schedulingService, times(0)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);


        verify(scheduledFutureHelper,times(0)).cancel(false);

        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }


    @Test()
    @DisplayName("Try scheduling reminder update (task with renewal date of today)")
    public void testTrySchedulingUpdatedReminderTWRDOT() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(2),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(1);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().minusYears(1).plusMinutes(5),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);


        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        verify(schedulingService, times(0)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);


        verify(scheduledFutureHelper,times(0)).cancel(false);
        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());

    }

    @Test
    @DisplayName("Try scheduling reminder update (previously registered reminder executes later and we need to reschedule it)")
    public void testTrySchedulingUpdatedReminderPRRELAWNTRI(){
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(1);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(0,6,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);
        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);

        //////////

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        /////////

        verify(scheduledFutureHelper,times(1)).cancel(false);
        verify(mockTaskScheduler,times(1)).schedule(
                eq(task2),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        assertEquals(cyclicalServiceEntity2,schedulingService.getLastRegisteredService());

    }


    @Test
    @DisplayName("Try scheduling reminder update (update to the reminder would make it fire later and the db holds  replacement)")
    public void testTrySchedulingUpdatedReminderUTTRWMIFL(){
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(1);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(3,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);


        CyclicalServiceEntity cyclicalServiceEntity3 = new CyclicalServiceEntity(
                "opis333",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(2,0,0)
        );
        cyclicalServiceEntity3.setId(1);

        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
        when(mockCyclicalServiceRepo.findFirstDateBefore(any(LocalDateTime.class))).thenReturn(Optional.of(cyclicalServiceEntity3));
        //////////

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        /////////

        verify(scheduledFutureHelper,times(1)).cancel(false);
        verify(mockTaskScheduler,times(0)).schedule(
                eq(task2),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler,times(2)).schedule(
                any(RunnableTask.class),
                any(Instant.class)
        );
        assertEquals(cyclicalServiceEntity3,schedulingService.getLastRegisteredService());

    }


    @Test
    @DisplayName("Try scheduling reminder update (update to the reminder would make it fire later and the db holds no replacement)")
    public void testTrySchedulingUpdatedReminderUTTRWMIFLATDHNR(){
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(1,0,0)
        );
        cyclicalServiceEntity1.setId(1);
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);

        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
                "opis222",1.2,
                LocalDateTime.now().plusYears(3),
                Period.of(3,0,0)
        );
        cyclicalServiceEntity2.setId(1);
        RunnableTask task2 = new RunnableTask("Reminder about: "+cyclicalServiceEntity2.toString(), mockCyclicalServiceRepo);


        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
        when(mockCyclicalServiceRepo.findFirstDateBefore(any(LocalDateTime.class))).thenReturn(Optional.empty());
        //////////

        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);

        /////////

        verify(scheduledFutureHelper,times(0)).cancel(false);
        verify(mockTaskScheduler,times(0)).schedule(
                eq(task2),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler,times(1)).schedule(
                any(RunnableTask.class),
                any(Instant.class)
        );
        assertEquals(cyclicalServiceEntity1,schedulingService.getLastRegisteredService());

    }
}
