package com.example.uslugicykliczne.configuration;

//import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.SchedulingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class CustomApplicationRunner implements ApplicationRunner {
    private final SchedulingService schedulingService;
    private final CyclicalServiceRepo cyclicalServiceRepo;

    public CustomApplicationRunner(SchedulingService schedulingService, CyclicalServiceRepo cyclicalServiceRepo) {
        this.schedulingService = schedulingService;
        this.cyclicalServiceRepo = cyclicalServiceRepo;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        cyclicalServiceRepo.customUpdateAwaitingRenewal();

//        List<CyclicalServiceEntity> cyclicalServiceEntityList = cyclicalServiceRepo.findAllDatesBeforeWithNoMessageSent(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1));
//
//        for(CyclicalServiceEntity cyclicalService:cyclicalServiceEntityList){
//            schedulingService.trySchedulingReminderWhenInserted(cyclicalService);
//        }
//        schedulingService.findNextServiceAndScheduleIt();

    }
}
