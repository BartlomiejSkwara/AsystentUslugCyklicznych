package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.BusinessDTO;
import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.repo.BusinessRepo;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepo businessRepo;
    private final ServiceUserRepo serviceUserRepo;
    private final ContactDataService contactDataService;
    private final EntityManager entityManager;




    public ResponseEntity<String> updateBusinessEntity(Integer id,BusinessDTO businessDTO){
        Optional<BusinessEntity> businessEntityOptional = businessRepo.findBusinessWithContactDataById(id);
        if (businessEntityOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent business !!!");

        ContactDataEntity contactDataEntity = businessEntityOptional.get().getContactData();
        contactDataService.updateContactDataEntity(contactDataEntity,businessDTO.getEmails(),businessDTO.getPhoneNumbers());

        businessRepo.save(createBusinessEntityFromDTO(businessEntityOptional.get(), businessDTO, contactDataEntity));
        return ResponseEntity.ok("Successfully updated the business");
    }

    public ResponseEntity<String> insertNewBusinessEntity(BusinessDTO businessDTO){

        ContactDataEntity contactDataEntity = contactDataService.insertContactDataEntity(businessDTO.getEmails(),businessDTO.getPhoneNumbers());
        businessRepo.save(createBusinessEntityFromDTO(new BusinessEntity() ,businessDTO,contactDataEntity));
        return ResponseEntity.ok("Successfully added the user");

    }
    public BusinessEntity createBusinessEntityFromDTO(BusinessEntity businessEntity, BusinessDTO dto, ContactDataEntity contactDataEntity ){
        businessEntity.setName(dto.getName());
        businessEntity.setComments(dto.getComments());
        businessEntity.setContactData(contactDataEntity);
        businessEntity.setAdres(dto.getAdres());
        businessEntity.setNip(dto.getNip());
        businessEntity.setRegon(dto.getRegon());
        return businessEntity;
    }

    public void deleteBusiness(Integer id) {
        Optional<BusinessEntity> businessEntity = businessRepo.findById(id);
        if (businessEntity.isPresent()){
            businessRepo.delete(businessEntity.get());
        }

    }

    public List<BusinessProjection> getAllRelatedToUser(Integer userID) {
        //List<BusinessEntity> businessGroup = businessRepo.find
        //return businessRepo.findBusinessByRelatedUserID(userID);
        var v = businessRepo.findBusinessRelatedToUserBy(entityManager.getReference(ServiceUserEntity.class,userID));
        if(v.isEmpty())
            return new ArrayList<>();
        return businessRepo.findBusinessWithProjectedContactDataFromBusinessGroup(v);
    }
}
