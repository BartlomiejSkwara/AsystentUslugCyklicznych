package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.CustomerDto;
import com.example.uslugicykliczne.dataTypes.DysponentDto;
import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.entity.DysponentEntity;
import com.example.uslugicykliczne.repo.DysponentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class DysponentService {
    private final DysponentRepo dysponentRepo;

    public DysponentService(DysponentRepo dysponentRepo) {
        this.dysponentRepo = dysponentRepo;
    }


    public ResponseEntity<String> updateDysponentEntity(Integer id,DysponentDto dysponentDto){
        Optional<DysponentEntity> dysponentEntity = dysponentRepo.findById(id);
        if (dysponentEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent dysponent !!!");
        convertDysponentDTOtoEntity(dysponentEntity.get(), dysponentDto);
        dysponentRepo.save(dysponentEntity.get());
        return ResponseEntity.ok("Successfully updated the dysponent");
    }

    public ResponseEntity<String> insertNewDysponentEntity(DysponentDto dysponentDto){
        List<DysponentEntity> duplicateUniques = dysponentRepo.findDysponentEntitiesByEmailOrPhoneNumberOrMfnSerialNumber(dysponentDto.getEmail(), dysponentDto.getPhoneNumber(), dysponentDto.getMfnSerialNumber());
        if(duplicateUniques.isEmpty()){
            dysponentRepo.save(convertDysponentDTOtoEntity(new DysponentEntity(),dysponentDto));
            return ResponseEntity.ok("Successfully added the user");
        }else {
            StringBuilder error = new StringBuilder("There can't be Dysponents with duplicate:");

            HashSet<String> duplicates = new HashSet<>();
            duplicateUniques.forEach(dysponentEntity ->
                    {
                        if(dysponentEntity.getEmail().equals(dysponentDto.getEmail()))
                            duplicates.add("[email]");
                        if(dysponentEntity.getPhoneNumber().equals(dysponentDto.getPhoneNumber()))
                            duplicates.add("[phone number]");
                        if(dysponentEntity.getMfnSerialNumber().equals(dysponentDto.getMfnSerialNumber()))
                            duplicates.add("[mfn serial number]");
                    }

            );

            duplicates.forEach(s ->
                    error.append(s)
            );

            return ResponseEntity.status(409).body(error.toString());
        }
    }
    public DysponentEntity convertDysponentDTOtoEntity(DysponentEntity dysponentEntity,DysponentDto dto){
        dysponentEntity.setEmail(dto.getEmail());
        dysponentEntity.setName(dto.getName());
        dysponentEntity.setSurname(dto.getSurname());
        dysponentEntity.setPhoneNumber(dto.getPhoneNumber());
        dysponentEntity.setMfnSerialNumber(dto.getMfnSerialNumber());
        return dysponentEntity;
    }
}
