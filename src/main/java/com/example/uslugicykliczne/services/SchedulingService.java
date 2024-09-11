//package com.example.uslugicykliczne.services;
//
//import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
//import com.example.uslugicykliczne.scheduling.RunnableTask;
//import com.example.uslugicykliczne.utility.TimeUtility;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.concurrent.ScheduledFuture;
//
//@Service
//public class SchedulingService {
//
//    private final CyclicalServiceRepo cyclicalServiceRepo;
//    private final TaskScheduler taskScheduler;
//    private ScheduledFuture<?> scheduledFuture;
//    private CyclicalServiceEntity lastRegisteredService;
//    private final EmailService emailService;
//
//    public ScheduledFuture<?> getScheduledFuture() {
//        return scheduledFuture;
//    }
//
//    public CyclicalServiceEntity getLastRegisteredService(){
//        return lastRegisteredService;
//    }
//    public Instant getLastRegisteredRenewal() {
//        if(lastRegisteredService == null)
//            return null;
//        return lastRegisteredService.getNextRenewal().toInstant(TimeUtility.getZoneOffset());
//    }
//
//    public SchedulingService(CyclicalServiceRepo cyclicalServiceRepo, TaskScheduler taskScheduler, EmailService emailService) {
//        this.cyclicalServiceRepo = cyclicalServiceRepo;
//        this.taskScheduler = taskScheduler;
//        this.emailService = emailService;
//    }
//
//    public void findNextServiceAndScheduleIt(){
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findFirstServiceWithNoMessageSent();
//        if(cyclicalServiceEntity.isPresent())
//            trySchedulingReminderWhenInserted(cyclicalServiceEntity.get());
//
//    }
//    public void trySchedulingReminderWhenInserted(CyclicalServiceEntity cyclicalServiceEntity)  {
//        RunnableTask runnableTask = new RunnableTask(cyclicalServiceEntity, cyclicalServiceRepo, this, emailService);
//        if(lastRegisteredService==null){
//            scheduledFuture = taskScheduler.schedule(
//                    runnableTask,
//                    cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset())
//            );
//            lastRegisteredService = cyclicalServiceEntity;
//            return;
//        }
//
//        Instant lastRenewal = getLastRegisteredRenewal();
//        Instant instantEntityNextRenewal = cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset());
//
//        if(instantEntityNextRenewal.isBefore(LocalDateTime.now().toInstant(TimeUtility.getZoneOffset()))||TimeUtility.isToday(instantEntityNextRenewal)){
//            runnableTask.run();
//            return;
//        }
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
//            /// szukam wcześniejszego w bd
//        }
//
//
//
//
//
//    }
//
//    public void trySchedulingReminderWhenUpdated(CyclicalServiceEntity cyclicalServiceEntity){
//
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
//
//    }
//
//    private void resetCurrentTaskInfo(){
//        this.scheduledFuture = null;
//        this.lastRegisteredService = null;
//    }
//
//
//}
