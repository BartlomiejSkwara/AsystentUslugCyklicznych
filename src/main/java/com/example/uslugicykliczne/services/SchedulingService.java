package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.scheduling.RunnableTask;
import com.example.uslugicykliczne.utility.TimeUtility;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulingService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;
    private Instant nextRegisteredRenewal;

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public Instant getNextRegisteredRenewal() {
        return nextRegisteredRenewal;
    }

    public SchedulingService(CyclicalServiceRepo cyclicalServiceRepo, TaskScheduler taskScheduler) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.taskScheduler = taskScheduler;
    }

    public void trySchedulingNewReminder(CyclicalServiceEntity cyclicalServiceEntity)  {
        Instant instantEntityNextRenewal = cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset());
        RunnableTask runnableTask = new RunnableTask("Reminder about: "+cyclicalServiceEntity.toString(), cyclicalServiceRepo);

        if(instantEntityNextRenewal.isBefore(LocalDateTime.now().toInstant(TimeUtility.getZoneOffset()))){
            runnableTask.run();
            return;
        }
        if(nextRegisteredRenewal==null){
            scheduledFuture = taskScheduler.schedule(
                    runnableTask,
                    cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset())
            );
            nextRegisteredRenewal = instantEntityNextRenewal;
        } else if (instantEntityNextRenewal.isBefore(nextRegisteredRenewal)) {
            scheduledFuture.cancel(false);
            scheduledFuture = taskScheduler.schedule(
                    runnableTask,
                    cyclicalServiceEntity.getNextRenewal().toInstant(TimeUtility.getZoneOffset())
            );
            nextRegisteredRenewal = instantEntityNextRenewal;

        } else {
            return;
        }





    }

    public void scheduleAllNearestReminders(){

    }

}
