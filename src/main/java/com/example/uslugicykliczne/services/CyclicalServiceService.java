package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CyclicalServiceService {

    private final CyclicalServiceRepo cyclicalServiceRepo;
//    private final SchedulingService schedulingService;

    public CyclicalServiceService(CyclicalServiceRepo cyclicalServiceRepo) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
    }
//
//
//
//    public ResponseEntity<String> renewCyclicalService(Integer id){
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findById(id);
//        if(cyclicalServiceEntity.isEmpty()){
//            return ResponseEntity.badRequest().body("Can't find the task you try to renew");
//        }
//
//        if(cyclicalServiceEntity.get().getRenewalMessageSent()){
//            schedulingService.trySchedulingReminderWhenInserted(cyclicalServiceEntity.get());
//
//        } else {
//            schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity.get());
//        }
//        return ResponseEntity.ok().body("The task was successfully renewed");
//    }
//
    public ResponseEntity<String> insertNewCyclicalServiceEntity(CyclicalServiceDto cyclicalServiceDto){
//        Optional<CustomerEntity> customerEntity = customerRepo.findById(cyclicalServiceDto.getCustomerId());
//        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(cyclicalServiceDto.getDysponentId());
//
//        if(customerEntity.isPresent() && dysponentEntity.isPresent()){
//            CyclicalServiceEntity insertedEntity = cyclicalServiceRepo.save(convertCyclicalServiceDTOtoEntity(new CyclicalServiceEntity(),cyclicalServiceDto,customerEntity.get(),dysponentEntity.get()));
//            schedulingService.trySchedulingReminderWhenInserted(insertedEntity);
//            return ResponseEntity.ok("Successfully added the cyclical service");
//        }
//
        StringBuilder stringBuilder = new StringBuilder("Entities :");
//        stringBuilder.append((customerEntity.isEmpty())?"[customer]":"");
//        stringBuilder.append((dysponentEntity.isEmpty())?"[dysponent]":"");
//        stringBuilder.append("with the provided ID(s) could not be found.");
        return  ResponseEntity.badRequest().body(stringBuilder.toString());
    }
//
//    public CyclicalServiceEntity convertCyclicalServiceDTOtoEntity(CyclicalServiceEntity cyclicalServiceEntity,CyclicalServiceDto dto, CustomerEntity customerEntity, DysponentEntity dysponentEntity){
//        cyclicalServiceEntity.setCustomerEntity(customerEntity);
//        cyclicalServiceEntity.setDysponentEntity(dysponentEntity);
//        cyclicalServiceEntity.setDescription(dto.getDescription());
//        cyclicalServiceEntity.setNextRenewal(dto.getNextRenewal());
//        cyclicalServiceEntity.setRenewalPeriod(dto.getRenewalPeriod());
//        cyclicalServiceEntity.setPrice(dto.getPrice());
//        cyclicalServiceEntity.setFirstCycleStart(dto.getFirstCycleStart());
//        return cyclicalServiceEntity;
//    }
//
//
//    public ResponseEntity<String> updateCyclicalServiceEntity(Integer id, CyclicalServiceDto cyclicalServiceDto){
//        Optional<CyclicalServiceEntity> cyclicalServiceEntity = cyclicalServiceRepo.findById(id);
//        if (cyclicalServiceEntity.isEmpty())
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent cyclical service !!!");
//
//
//        Optional<CustomerEntity> customerEntity = customerRepo.findById(cyclicalServiceDto.getCustomerId());
//        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(cyclicalServiceDto.getDysponentId());
//
//        if(customerEntity.isPresent() && dysponentEntity.isPresent()){
//            CyclicalServiceEntity cyclicalServiceEntity1 = cyclicalServiceRepo.save(convertCyclicalServiceDTOtoEntity(cyclicalServiceEntity.get(),cyclicalServiceDto,customerEntity.get(),dysponentEntity.get()));
//            schedulingService.trySchedulingReminderWhenUpdated(cyclicalServiceEntity1);
//            return ResponseEntity.ok("Successfully updated the cyclical service");
//        }
//
//        StringBuilder stringBuilder = new StringBuilder("Entities :");
//        stringBuilder.append((customerEntity.isEmpty())?"[customer]":"");
//        stringBuilder.append((dysponentEntity.isEmpty())?"[dysponent]":"");
//        stringBuilder.append("with the provided ID(s) could not be found.");
//        return  ResponseEntity.badRequest().body(stringBuilder.toString());
//
//
//
//    }
//
//
}
