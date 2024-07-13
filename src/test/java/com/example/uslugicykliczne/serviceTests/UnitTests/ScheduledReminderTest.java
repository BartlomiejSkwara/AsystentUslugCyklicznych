package com.example.uslugicykliczne.serviceTests.UnitTests;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.services.SchedulingService;
import com.example.uslugicykliczne.utility.TimeUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
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

    @InjectMocks
    private SchedulingService schedulingService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        schedulingService = new SchedulingService(mockCyclicalServiceRepo, mockTaskScheduler);
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

        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity);

        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task)),
                eq(cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(0)).cancel(false);

        assertEquals(cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getNextRegisteredRenewal());

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

        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity1);
        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity2);



        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task1)),
                eq(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler, times(0)).schedule(
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(0)).cancel(false);
        assertEquals( cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getNextRegisteredRenewal());

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

        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity1);
        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity2);



        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task1)),
                eq(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(mockTaskScheduler, times(1)).schedule(
                argThat(arg -> arg.equals(task2)),
                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        verify(scheduledFutureHelper,times(1)).cancel(false);
        assertEquals(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getNextRegisteredRenewal());

    }


    @Test()
    @DisplayName("Try scheduling reminder (task with renewal in past)")
    public void testTrySchedulingNewReminderTWRIP() {
        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
                "opis",1.2,
                LocalDateTime.now().minusYears(2),
                Period.of(1,0,0)
        );
        RunnableTask task1 = new RunnableTask("Reminder about: "+cyclicalServiceEntity1.toString(), mockCyclicalServiceRepo);


        schedulingService.trySchedulingNewReminder(cyclicalServiceEntity1);


        verify(scheduledFutureHelper,times(0)).cancel(false);
        verify(mockTaskScheduler, times(0)).schedule(
                argThat(arg -> arg.equals(task1)),
                eq(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
        );
        assertNull(schedulingService.getNextRegisteredRenewal());

    }


}
