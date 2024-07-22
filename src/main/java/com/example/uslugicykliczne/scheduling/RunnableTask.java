package com.example.uslugicykliczne.scheduling;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.EmailService;
import com.example.uslugicykliczne.services.SchedulingService;

import java.util.Date;
import java.util.Objects;

public class RunnableTask implements Runnable{
    private final CyclicalServiceEntity cyclicalServiceEntity;
    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final SchedulingService schedulingService;

    private final EmailService emailService;
    public RunnableTask(CyclicalServiceEntity cyclicalServiceEntity, CyclicalServiceRepo cyclicalServiceRepo, SchedulingService schedulingService, EmailService emailService){
        this.cyclicalServiceEntity = cyclicalServiceEntity;
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.schedulingService = schedulingService;
        this.emailService = emailService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunnableTask that)) return false;
        return Objects.equals(cyclicalServiceEntity, that.cyclicalServiceEntity) && Objects.equals(cyclicalServiceRepo, that.cyclicalServiceRepo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cyclicalServiceEntity, cyclicalServiceRepo);
    }

    @Override
    public void run() {
        cyclicalServiceEntity.setNextRenewal(cyclicalServiceEntity.getNextRenewal().plus(cyclicalServiceEntity.getRenewalPeriod()));
        cyclicalServiceEntity.setRenewalMessageSent(true);
        cyclicalServiceRepo.save(cyclicalServiceEntity);
        schedulingService.findNextServiceAndScheduleIt();
        emailService.sendEmailNotification("Cyclical service renewal time is near !", cyclicalServiceEntity.toString());

        //System.out.println(new Date()+" Runnable Task with "+cyclicalServiceEntity.toString()+" on thread "+Thread.currentThread().getName());
    }


}
