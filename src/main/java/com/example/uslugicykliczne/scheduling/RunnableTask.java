package com.example.uslugicykliczne.scheduling;

import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CertificateRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.EmailSenderService;
import com.example.uslugicykliczne.services.EmailService;
import com.example.uslugicykliczne.services.SchedulingService;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode
public class RunnableTask implements Runnable{
    private final CyclicalServiceEntity cyclicalServiceEntity;
    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final CertificateRepo certificateRepo;
    private final SchedulingService schedulingService;
    private final CertificateEntity certificateEntity;

    private final EmailSenderService emailSenderService;
    public RunnableTask(CyclicalServiceEntity cyclicalServiceEntity, CertificateEntity certificateEntity, CyclicalServiceRepo cyclicalServiceRepo, CertificateRepo certificateRepo, SchedulingService schedulingService, EmailSenderService emailSenderService){
        this.cyclicalServiceEntity = cyclicalServiceEntity;
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.schedulingService = schedulingService;
        this.emailSenderService = emailSenderService;
        this.certificateEntity = certificateEntity;
        this.certificateRepo = certificateRepo;
    }


    @Override
    public void run() {
//        cyclicalServiceEntity.setNextRenewal(cyclicalServiceEntity.getNextRenewal().plus(cyclicalServiceEntity.getRenewalPeriod()));
        certificateEntity.setRenewalMessageSent(true);
        certificateRepo.save(certificateEntity);
        schedulingService.findNextServiceAndScheduleIt();
        emailSenderService.sendEmailNotification("Cyclical service renewal time is near !", cyclicalServiceEntity);

        //System.out.println(new Date()+" Runnable Task with "+cyclicalServiceEntity.toString()+" on thread "+Thread.currentThread().getName());
    }

    public void searchlessRun(){
        certificateEntity.setRenewalMessageSent(true);
        certificateRepo.save(certificateEntity);
        emailSenderService.sendEmailNotification("Cyclical service renewal time is near !", cyclicalServiceEntity);

    }


}
