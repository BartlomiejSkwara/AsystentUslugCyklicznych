package com.example.uslugicykliczne.configuration;

//import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.entity.StatusTypeEntity;
import com.example.uslugicykliczne.repo.AccountDataRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import com.example.uslugicykliczne.repo.StatusTypeRepo;
import com.example.uslugicykliczne.services.ContactDataService;
import com.example.uslugicykliczne.services.SchedulingService;
import com.example.uslugicykliczne.services.StartupService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomApplicationRunner implements ApplicationRunner {

    private final StartupService startupService;
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        startupService.start();

    }


}
