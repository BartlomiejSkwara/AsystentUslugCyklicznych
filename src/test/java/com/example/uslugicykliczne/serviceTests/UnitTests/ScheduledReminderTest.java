package com.example.uslugicykliczne.serviceTests.UnitTests;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.services.EmailSenderService;
import com.example.uslugicykliczne.services.SchedulingService;
import com.example.uslugicykliczne.utility.TimeUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.scheduling.TaskScheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class ScheduledReminderTest {
    @Mock
    private TaskScheduler mockTaskScheduler;

    @Mock
    EmailSenderService mockEmailSender;
    @Mock
    private CyclicalServiceRepo mockCSRepo;

    @Mock
    private CertificateRepo mockCerRepo;
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
    @DisplayName("Find next and schedule success")
    public void testTryFindingNextAndScheduling1(){
        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusDays(10),false,false);

        when(mockCerRepo.findFirstMostRecentCertificateWithoutMessageSent()).thenReturn(Optional.of(ce1));

        schedulingService.findNextServiceAndScheduleIt();
        verify(schedulingService).findNextServiceAndScheduleIt();
        verify(schedulingService).trySchedulingReminderWhenInserted(ce1,cs1);

        doNothing().when(schedulingService).trySchedulingReminderWhenInserted(ce1,cs1);

//        verifyNoMoreInteractions(schedulingService,mockTaskScheduler,mockEmailSender,mockCSRepo,mockCerRepo,scheduledFutureHelper,runnableTaskHelper);

    }
    @Test()
    @DisplayName("Find next and schedule failure")
    public void testTryFindingNextAndScheduling2(){
        when(mockCerRepo.findFirstMostRecentCertificateWithoutMessageSent()).thenReturn(Optional.empty());

        schedulingService.findNextServiceAndScheduleIt();
        verify(schedulingService).findNextServiceAndScheduleIt();
        verify(schedulingService, times(0)).trySchedulingReminderWhenInserted(ArgumentMatchers.any(CertificateEntity.class),ArgumentMatchers.any(CyclicalServiceEntity.class));

//        verifyNoMoreInteractions(schedulingService,mockTaskScheduler,mockEmailSender,mockCSRepo,mockCerRepo,scheduledFutureHelper,runnableTaskHelper);
    }
    @Test()
    @DisplayName("Try insert scheduling reminder (no reminders before)")
    public void testTryInsertSchedulingNewReminderNRB() {
        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusDays(10),false,false);

        RunnableTask rt1 = new RunnableTask(cs1,ce1, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);



        schedulingService.trySchedulingReminderWhenInserted(ce1,cs1);
        verify(schedulingService).trySchedulingReminderWhenInserted(ce1,cs1);
        verify(schedulingService).getLastRegisteredCertificate();
        verify(mockTaskScheduler, times(1)).schedule(
                rt1,
                ce1.getValidTo().toInstant(TimeUtility.getZoneOffset())
        );


        verifyNoMoreInteractions(schedulingService,mockTaskScheduler,mockEmailSender,mockCSRepo,mockCerRepo,scheduledFutureHelper,runnableTaskHelper);
        when(schedulingService.getLastRegisteredCertificate()).thenCallRealMethod();
        assertEquals(cs1,schedulingService.getLastRegisteredCyclicalService());
        assertEquals(ce1,schedulingService.getLastRegisteredCertificate());

    }

    @Test()
    @DisplayName("Try insert scheduling reminder (last reminder executes earlier(do nothing))")
    public void testTryInsertSchedulingNewReminderLREE() {
        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusYears(1),false,false);
        RunnableTask rt1 = new RunnableTask(cs1,ce1, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);

        CyclicalServiceEntity cs2 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce2 = TestUtilityService.createCertificateEntity(cs2,LocalDateTime.now().plusYears(2),false,false);
        RunnableTask rt2 = new RunnableTask(cs2,ce2, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);


        when(schedulingService.getLastRegisteredCertificate()).thenReturn(ce1);

        schedulingService.trySchedulingReminderWhenInserted(ce2,cs2);

        verify(schedulingService).trySchedulingReminderWhenInserted(ce2,cs2);
        verify(schedulingService,times(2)).getLastRegisteredCertificate();
        verifyNoMoreInteractions(schedulingService,mockTaskScheduler,mockEmailSender,mockCSRepo,mockCerRepo,scheduledFutureHelper,runnableTaskHelper);

        when(schedulingService.getLastRegisteredCertificate()).thenCallRealMethod();
        assertNotEquals(cs2,schedulingService.getLastRegisteredCyclicalService());
        assertNotEquals(ce2,schedulingService.getLastRegisteredCertificate());
    }
    @Test()
    @DisplayName("Try insert scheduling reminder (last reminder executes later (change and cancel))")
    public void testTryInsertSchedulingNewReminderLREL() {
        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusYears(3),false,false);
        RunnableTask rt1 = new RunnableTask(cs1,ce1, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);

        CyclicalServiceEntity cs2 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce2 = TestUtilityService.createCertificateEntity(cs2,LocalDateTime.now().plusYears(2),false,false);
        RunnableTask rt2 = new RunnableTask(cs2,ce2, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);

        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
        when(schedulingService.getLastRegisteredCertificate()).thenReturn(ce1);
        when(schedulingService.getScheduledFuture()).thenReturn(scheduledFutureHelper);

        schedulingService.trySchedulingReminderWhenInserted(ce2,cs2);



        verify(schedulingService).trySchedulingReminderWhenInserted(ce2,cs2);
        verify(schedulingService,times(2)).getLastRegisteredCertificate();
        verify(schedulingService).getScheduledFuture();
        verify(scheduledFutureHelper).cancel(false);
        verify(mockTaskScheduler, times(1)).schedule(
                rt2,
                ce2.getValidTo().toInstant(TimeUtility.getZoneOffset())
        );
        verifyNoMoreInteractions(schedulingService,mockTaskScheduler,mockEmailSender,mockCSRepo,mockCerRepo,scheduledFutureHelper,runnableTaskHelper);

        //Final check
        when(schedulingService.getLastRegisteredCertificate()).thenCallRealMethod();
        assertEquals(cs2,schedulingService.getLastRegisteredCyclicalService());
        assertEquals(ce2,schedulingService.getLastRegisteredCertificate());
    }


    @Test()
    @DisplayName("Try insert scheduling reminder (task with renewal in past)")
    public void testTryInsertSchedulingNewReminderTWRIP() {
        CyclicalServiceEntity cs1 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce1 = TestUtilityService.createCertificateEntity(cs1,LocalDateTime.now().plusYears(3),false,false);
        RunnableTask rt1 = new RunnableTask(cs1,ce1, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);

        CyclicalServiceEntity cs2 = TestUtilityService.createCyclicalServiceEntity(false,null,null);
        CertificateEntity ce2 = TestUtilityService.createCertificateEntity(cs2,LocalDateTime.now().minusDays(1),false,false);
        RunnableTask rt2 = new RunnableTask(cs2,ce2, mockCSRepo,mockCerRepo, schedulingService, mockEmailSender);

        when(schedulingService.getLastRegisteredCertificate()).thenReturn(ce1);


        schedulingService.trySchedulingReminderWhenInserted(ce2,cs2);

//        schedulingService.get


        verify(schedulingService).trySchedulingReminderWhenInserted(ce2,cs2);
        verify(schedulingService,times(2)).getLastRegisteredCertificate();


        //Final check
        when(schedulingService.getLastRegisteredCertificate()).thenCallRealMethod();
        assertEquals(null,schedulingService.getLastRegisteredCyclicalService());
        assertEquals(null,schedulingService.getLastRegisteredCertificate());
    }

//
//    @Test
//    @DisplayName("Try scheduling reminder update when the updated entity is not the last registered one")
//    public void testTrySchedulingUpdatedReminderWTUEINTLRO(){
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(0);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().minusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        verify(schedulingService, times(1)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);
//    }
//
//
//
//    @Test()
//    @DisplayName("Try scheduling reminder update (task with renewal in past)")
//    public void testTrySchedulingUpdatedReminderTWRIP() {
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(1);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().minusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        verify(schedulingService, times(0)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);
//
//
//        verify(scheduledFutureHelper,times(0)).cancel(false);
//
//        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());
//
//    }
//
//
//    @Test()
//    @DisplayName("Try scheduling reminder update (task with renewal date of today)")
//    public void testTrySchedulingUpdatedReminderTWRDOT() {
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(2),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(1);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().minusYears(1).plusMinutes(5),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        verify(schedulingService, times(0)).trySchedulingReminderWhenInserted(cyclicalServiceEntity2);
//
//
//        verify(scheduledFutureHelper,times(0)).cancel(false);
//        assertEquals(cyclicalServiceEntity1.getNextRenewal().toInstant(TimeUtility.getZoneOffset()),schedulingService.getLastRegisteredRenewal());
//
//    }
//
//    @Test
//    @DisplayName("Try scheduling reminder update (previously registered reminder executes later and we need to reschedule it)")
//    public void testTrySchedulingUpdatedReminderPRRELAWNTRI(){
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(1);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(0,6,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
//
//        //////////
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        /////////
//
//        verify(scheduledFutureHelper,times(1)).cancel(false);
//        verify(mockTaskScheduler,times(1)).schedule(
//                eq(task2),
//                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
//        );
//        assertEquals(cyclicalServiceEntity2,schedulingService.getLastRegisteredService());
//
//    }
//
//
//    @Test
//    @DisplayName("Try scheduling reminder update (update to the reminder would make it fire later and the db holds  replacement)")
//    public void testTrySchedulingUpdatedReminderUTTRWMIFL(){
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(1);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(3,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//
//
//        CyclicalServiceEntity cyclicalServiceEntity3 = new CyclicalServiceEntity(
//                "opis333",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(2,0,0)
//        );
//        cyclicalServiceEntity3.setId(1);
//
//        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
//        when(mockCyclicalServiceRepo.findFirstDateBefore(any(LocalDateTime.class))).thenReturn(Optional.of(cyclicalServiceEntity3));
//        //////////
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        /////////
//
//        verify(scheduledFutureHelper,times(1)).cancel(false);
//        verify(mockTaskScheduler,times(0)).schedule(
//                eq(task2),
//                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
//        );
//        verify(mockTaskScheduler,times(2)).schedule(
//                any(RunnableTask.class),
//                any(Instant.class)
//        );
//        assertEquals(cyclicalServiceEntity3,schedulingService.getLastRegisteredService());
//
//    }
//
//
//    @Test
//    @DisplayName("Try scheduling reminder update (update to the reminder would make it fire later and the db holds no replacement)")
//    public void testTrySchedulingUpdatedReminderUTTRWMIFLATDHNR(){
//        CyclicalServiceEntity cyclicalServiceEntity1 = new CyclicalServiceEntity(
//                "opis",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(1,0,0)
//        );
//        cyclicalServiceEntity1.setId(1);
//        RunnableTask task1 = new RunnableTask(cyclicalServiceEntity1, mockCyclicalServiceRepo, schedulingService, emailService);
//
//        CyclicalServiceEntity cyclicalServiceEntity2 = new CyclicalServiceEntity(
//                "opis222",1.2,
//                LocalDateTime.now().plusYears(3),
//                Period.of(3,0,0)
//        );
//        cyclicalServiceEntity2.setId(1);
//        RunnableTask task2 = new RunnableTask(cyclicalServiceEntity2, mockCyclicalServiceRepo, schedulingService, emailService);
//
//
//        when(mockTaskScheduler.schedule(any(RunnableTask.class),any(Instant.class))).thenReturn(scheduledFutureHelper);
//        when(mockCyclicalServiceRepo.findFirstDateBefore(any(LocalDateTime.class))).thenReturn(Optional.empty());
//        //////////
//
//        schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity1);
//        schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity2);
//
//        /////////
//
//        verify(scheduledFutureHelper,times(0)).cancel(false);
//        verify(mockTaskScheduler,times(0)).schedule(
//                eq(task2),
//                eq(cyclicalServiceEntity2.getNextRenewal().toInstant(TimeUtility.getZoneOffset()))
//        );
//        verify(mockTaskScheduler,times(1)).schedule(
//                any(RunnableTask.class),
//                any(Instant.class)
//        );
//        assertEquals(cyclicalServiceEntity1,schedulingService.getLastRegisteredService());
//
//    }
}
