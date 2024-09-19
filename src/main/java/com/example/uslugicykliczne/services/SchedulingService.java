package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.utility.TimeUtility;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulingService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final CertificateRepo certificateRepo;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;
    private CertificateEntity lastRegisteredCertificate;
    private CyclicalServiceEntity lastRegisteredCyclicalService;

    private final EmailSenderService emailSenderService;

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public CertificateEntity getLastRegisteredCertificate(){
        return lastRegisteredCertificate;
    }
    public CyclicalServiceEntity getLastRegisteredCyclicalService(){return  lastRegisteredCyclicalService;}
    public Instant getLastRegisteredRenewal() {
        if(lastRegisteredCertificate == null)
            return null;
        return lastRegisteredCertificate.getValidTo().toInstant(TimeUtility.getZoneOffset());
    }

    public SchedulingService(CyclicalServiceRepo cyclicalServiceRepo, CertificateRepo certificateRepo, TaskScheduler taskScheduler,  EmailSenderService emailSenderService) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.certificateRepo = certificateRepo;
        this.taskScheduler = taskScheduler;
        this.emailSenderService = emailSenderService;
    }

    public void findNextServiceAndScheduleIt(){
        Optional<CertificateEntity> certificateEntity = certificateRepo.findFirstCertificateWithNoRenewalAndMessageSent();
        if(certificateEntity.isPresent())
            trySchedulingReminderWhenInserted(certificateEntity.get(),certificateEntity.get().getCyclicalServiceEntity());
    }
    public void trySchedulingReminderWhenInserted(CertificateEntity certificateEntity,CyclicalServiceEntity cyclicalServiceEntity)  {
        RunnableTask runnableTask = new RunnableTask(cyclicalServiceEntity, certificateEntity, cyclicalServiceRepo, certificateRepo,this, emailSenderService);
        if(lastRegisteredCertificate==null){
            scheduledFuture = taskScheduler.schedule(
                    runnableTask,
                    certificateEntity.getValidTo().toInstant(TimeUtility.getZoneOffset())
            );
            lastRegisteredCertificate = certificateEntity;
            lastRegisteredCyclicalService = cyclicalServiceEntity;
            return;
        }

        Instant lastRenewal = getLastRegisteredRenewal();
        Instant instantEntityNextRenewal = certificateEntity.getValidTo().toInstant(TimeUtility.getZoneOffset());

        if(instantEntityNextRenewal.isBefore(LocalDateTime.now().toInstant(TimeUtility.getZoneOffset()))||TimeUtility.isToday(instantEntityNextRenewal)){
            runnableTask.run();
            return;
        }

        if (instantEntityNextRenewal.isBefore(lastRenewal)) {
            scheduledFuture.cancel(false);
            scheduledFuture = taskScheduler.schedule(
                    runnableTask,
                    certificateEntity.getValidTo().toInstant(TimeUtility.getZoneOffset())
            );
            lastRegisteredCertificate = certificateEntity;
            lastRegisteredCyclicalService = cyclicalServiceEntity;

        } else if(TimeUtility.isSameDay(instantEntityNextRenewal, lastRenewal)){
            return;
        }

    }

    public void trySchedulingReminderWhenUpdated(CertificateEntity certificateEntity, CyclicalServiceEntity cyclicalServiceEntity){

        if(lastRegisteredCyclicalService.getIdCyclicalService() == cyclicalServiceEntity.getIdCyclicalService()){
            scheduledFuture.cancel(false);
            lastRegisteredCertificate.setRenewalMessageSent(true);
            certificateRepo.save(lastRegisteredCertificate);
            lastRegisteredCertificate = null;
            lastRegisteredCyclicalService = null;
        }
        trySchedulingReminderWhenInserted(certificateEntity,cyclicalServiceEntity);
//        if(!cyclicalServiceEntity.getId().equals(lastRegisteredService.getId())){
//            trySchedulingReminderWhenInserted(cyclicalServiceEntity);
//            return;
//        }
//
//
//        Instant lastRenewal = getLastRegisteredRenewal();
//        Instant instantEntityNextRenewal = cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset());
//        RunnableTask runnableTask = new RunnableTask(cyclicalServiceEntity, cyclicalServiceRepo, this, emailService);
//
//        if(instantEntityNextRenewal.isBefore(LocalDateTime.now().toInstant(TimeUtility.getZoneOffset()))||TimeUtility.isToday(instantEntityNextRenewal)){
//            runnableTask.run();
//            return;
//        }
//
//
//        if (instantEntityNextRenewal.isBefore(lastRenewal)) {
//            scheduledFuture.cancel(false);
//            scheduledFuture = taskScheduler.schedule(
//                    runnableTask,
//                    cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset())
//            );
//            lastRegisteredService = cyclicalServiceEntity;
//
//        } else if(TimeUtility.isSameDay(instantEntityNextRenewal, lastRenewal)){
//            return;
//        } else {
//
//
//            /// Checking if after postponing current task there is another thing that should fire earlier
//            Optional<CyclicalServiceEntity> replacement  = cyclicalServiceRepo.findFirstDateBeforeWithNoMessageSent(lastRegisteredService.getNextRenewal());
//            if(replacement.isPresent()){
//                scheduledFuture.cancel(false);
//                RunnableTask runnableTask2 = new RunnableTask(replacement.get(), cyclicalServiceRepo, this, emailService);
//                scheduledFuture = taskScheduler.schedule(
//                        runnableTask,
//                        replacement.get().getNextRenewal().toInstant(TimeUtility.getZoneOffset())
//                );
//                lastRegisteredService = replacement.get();
//
//            }
//
//
//        }
//

    }

    private void resetCurrentTaskInfo(){
        this.scheduledFuture = null;
        this.lastRegisteredCertificate = null;
        this.lastRegisteredCyclicalService = null;
    }


}
