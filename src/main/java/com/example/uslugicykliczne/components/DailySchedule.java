package com.example.uslugicykliczne.components;

import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.CyclicalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailySchedule {
    private final CyclicalServiceRepo cyclicalServiceRepo;

    @Scheduled(cron = "0 0 0 * * ?")
    public void daily(){
        cyclicalServiceRepo.removeExpiredIgnores();
        cyclicalServiceRepo.customUpdateAwaitingRenewal();
        cyclicalServiceRepo.setExpiredStatus();

    }

}
