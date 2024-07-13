package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.DysponentEntity;
import com.example.uslugicykliczne.repo.CustomerRepo;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.repo.DysponentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CyclicalServiceService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final CustomerRepo customerRepo;
    private final DysponentRepo dysponentRepo;
    private final SchedulingService schedulingService;

    public CyclicalServiceService(CyclicalServiceRepo cyclicalServiceRepo, CustomerRepo customerRepo, DysponentRepo dysponentRepo, SchedulingService schedulingService) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.customerRepo = customerRepo;
        this.dysponentRepo = dysponentRepo;
        this.schedulingService = schedulingService;
    }


    public ResponseEntity<String> insertNewCyclicalServiceEntity(CyclicalServiceDto cyclicalServiceDto){
        Optional<CustomerEntity> customerEntity = customerRepo.findById(cyclicalServiceDto.getCustomerId());
        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(cyclicalServiceDto.getDysponentId());

        if(customerEntity.isPresent() && dysponentEntity.isPresent()){
            CyclicalServiceEntity insertedEntity = cyclicalServiceRepo.save(convertCyclicalServiceDTOtoEntity(new CyclicalServiceEntity(),cyclicalServiceDto,customerEntity.get(),dysponentEntity.get()));
            schedulingService.trySchedulingNewReminder(insertedEntity);
            return ResponseEntity.ok("Successfully added the cyclical service");
        }

        StringBuilder stringBuilder = new StringBuilder("Entities :");
        stringBuilder.append((customerEntity.isEmpty())?"[customer]":"");
        stringBuilder.append((dysponentEntity.isEmpty())?"[dysponent]":"");
        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());
    }

    public CyclicalServiceEntity convertCyclicalServiceDTOtoEntity(CyclicalServiceEntity cyclicalServiceEntity,CyclicalServiceDto dto, CustomerEntity customerEntity, DysponentEntity dysponentEntity){
        cyclicalServiceEntity.setCustomerEntity(customerEntity);
        cyclicalServiceEntity.setDysponentEntity(dysponentEntity);
        cyclicalServiceEntity.setDescription(dto.getDescription());
        cyclicalServiceEntity.setNextRenewal(dto.getNextRenewal());
        cyclicalServiceEntity.setRenewalPeriod(dto.getRenewalPeriod());
        cyclicalServiceEntity.setPrice(dto.getPrice());
        cyclicalServiceEntity.setFirstCycleStart(dto.getFirstCycleStart());
        return cyclicalServiceEntity;
    }


    public ResponseEntity<String> updateCyclicalServiceEntity(Integer id, CyclicalServiceDto cyclicalServiceDto){
        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findById(id);
        if (cyclicalServiceEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent cyclical service !!!");


        Optional<CustomerEntity> customerEntity = customerRepo.findById(cyclicalServiceDto.getCustomerId());
        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(cyclicalServiceDto.getDysponentId());

        if(customerEntity.isPresent() && dysponentEntity.isPresent()){
            CyclicalServiceEntity cyclicalServiceEntity1 = cyclicalServiceRepo.save(convertCyclicalServiceDTOtoEntity(cyclicalServiceEntity.get(),cyclicalServiceDto,customerEntity.get(),dysponentEntity.get()));
            schedulingService.trySchedulingNewReminder(cyclicalServiceEntity1);
            return ResponseEntity.ok("Successfully updated the cyclical service");
        }

        StringBuilder stringBuilder = new StringBuilder("Entities :");
        stringBuilder.append((customerEntity.isEmpty())?"[customer]":"");
        stringBuilder.append((dysponentEntity.isEmpty())?"[dysponent]":"");
        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());



    }


}
