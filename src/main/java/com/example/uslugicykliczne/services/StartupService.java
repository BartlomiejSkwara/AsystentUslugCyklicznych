package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.entity.StatusTypeEntity;
import com.example.uslugicykliczne.repo.AccountDataRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import com.example.uslugicykliczne.repo.StatusTypeRepo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {
//    private final SchedulingService schedulingService;
    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final AccountDataRepo accountDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final ContactDataService contactDataService;
    private final Environment env;
    private final ServiceUserRepo serviceUserRepo;
    private final StatusTypeRepo statusTypeRepo;
    private final EntityManager entityManager;

    public void start() {
        System.out.println(accountDataRepo.count());
        if(accountDataRepo.count()<1) {
            System.out.println("No users, creating admin ....");

            AccountDataEntity accountDataEntity = new AccountDataEntity();
            accountDataEntity.setRole("ROLE_admin");
            accountDataEntity.setUsername(env.getProperty("conf.admin.login"));

            accountDataEntity.setHashedPassword(passwordEncoder.encode(env.getProperty("conf.admin.password")));


            ServiceUserEntity serviceUserEntity = new ServiceUserEntity();
            serviceUserEntity.setAccountDataEntity(accountDataRepo.save(accountDataEntity));
            serviceUserEntity.setSurname("admin");
            serviceUserEntity.setName("admin");
            serviceUserEntity.setContactData(contactDataService.insertContactDataEntity(List.of("blank@email"),List.of("123")));
            serviceUserEntity.setHasPolishPesel(false);
            serviceUserRepo.save(serviceUserEntity);
        }

        if(statusTypeRepo.count()<1){
            System.out.println("No statuses, inserting defaults ....");

            statusTypeRepo.save(createStatusType(1,"AWAITING_RENEWAL"));
            statusTypeRepo.save(createStatusType(2,"PRO_FORM_SENT"));
            statusTypeRepo.save(createStatusType(4,"MARKED_FOR_CANCEL"));
            statusTypeRepo.save(createStatusType(8,"CANCELED"));
            statusTypeRepo.save(createStatusType(16,"MARKED_AS_NON_RENEWABLE"));
            statusTypeRepo.save(createStatusType(32,"RENEWED_ELSEWHERE"));
            statusTypeRepo.save(createStatusType(64,"PAYMENT_DONE"));
            statusTypeRepo.save(createStatusType(128,"INVOICE_SENT"));
            statusTypeRepo.save(createStatusType(256,"RENEWED"));
            statusTypeRepo.save(createStatusType(512,"NEW"));
            statusTypeRepo.save(createStatusType(1024,"IGNORE"));
            statusTypeRepo.save(createStatusType(2048,"EXPIRED"));

        }
        cyclicalServiceRepo.removeExpiredIgnores();
        cyclicalServiceRepo.customUpdateAwaitingRenewal();
        cyclicalServiceRepo.setExpiredStatus();

    }

    private StatusTypeEntity createStatusType(Integer value, String name) {
        StatusTypeEntity statusTypeEntity1 = new StatusTypeEntity();
        statusTypeEntity1.setName(name);
        statusTypeEntity1.setIdStatusType(value);
        return statusTypeEntity1;
    }

}
