package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.BusinessDTO;
import com.example.uslugicykliczne.dataTypes.projections.BusinessProjection;
import com.example.uslugicykliczne.entity.*;
import com.example.uslugicykliczne.repo.AddressRepo;
import com.example.uslugicykliczne.repo.BusinessRepo;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepo businessRepo;
    private final ServiceUserRepo serviceUserRepo;
    private final ContactDataService contactDataService;
    private final EntityManager entityManager;
    private final AddressRepo addressRepo;


    public ResponseEntity<String> updateBusinessEntity(Integer id,BusinessDTO businessDTO,boolean duplicateCheck){
        Optional<BusinessEntity> businessEntityOptional = businessRepo.findBusinessWithContactDataById(id);
        if (businessEntityOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie można edytować nieistniejącej firmy !!!");


        ContactDataEntity contactDataEntity = businessEntityOptional.get().getContactData();

        if(duplicateCheck){
            HashSet<String> foundDuplicates = contactDataService.findContactsInDB(businessDTO.getEmails(),businessDTO.getPhoneNumbers());
            for(EmailEntity em : contactDataEntity.getEmails()){
                foundDuplicates.remove(em.getEmail());
            }
            for(PhoneNumberEntity em : contactDataEntity.getPhoneNumbers()){
                foundDuplicates.remove(em.getNumber());
            }
            if(!foundDuplicates.isEmpty()){
                return ResponseEntity.badRequest().body("Podano już zapisane dane kontaktowe: "+foundDuplicates);
            }
        }

        AddressEntity addressEntity = createAddressEntityFromDTO(businessEntityOptional.get().getAddress(),businessDTO);
        addressRepo.save(addressEntity);

        contactDataService.updateContactDataEntity(contactDataEntity,businessDTO.getEmails(),businessDTO.getPhoneNumbers());

        businessRepo.save(createBusinessEntityFromDTO(businessEntityOptional.get(), businessDTO, contactDataEntity,addressEntity));
        return ResponseEntity.ok("Z powodzeniem zaktualizowano dane firmy !!!");
    }

    public ResponseEntity<String> insertNewBusinessEntity(BusinessDTO businessDTO,boolean duplicateCheck){

        if(duplicateCheck){
            HashSet<String> foundDuplicates = contactDataService.findContactsInDB(businessDTO.getEmails(),businessDTO.getPhoneNumbers());
            if(!foundDuplicates.isEmpty()){
                return ResponseEntity.badRequest().body("Podano zarejestrowane już dane kontaktowe: "+foundDuplicates);
            }
        }
        AddressEntity addressEntity = createAddressEntityFromDTO(new AddressEntity(),businessDTO);
        addressRepo.save(addressEntity);
        ContactDataEntity contactDataEntity = contactDataService.insertContactDataEntity(businessDTO.getEmails(),businessDTO.getPhoneNumbers());

        businessRepo.save(createBusinessEntityFromDTO(new BusinessEntity() ,businessDTO,contactDataEntity,addressEntity));
        return ResponseEntity.ok("Z powodzeniem dodano firmę");

    }
    public BusinessEntity createBusinessEntityFromDTO(BusinessEntity businessEntity, BusinessDTO dto, ContactDataEntity contactDataEntity,AddressEntity addressEntity ){
        businessEntity.setName(dto.getName().trim());
        businessEntity.setComments(dto.getComments());
        businessEntity.setContactData(contactDataEntity);
        businessEntity.setAddress(addressEntity);
        businessEntity.setNip(dto.getNip());
        if(dto.getRegon().isPresent()&&!dto.getRegon().get().isEmpty())
            businessEntity.setRegon(dto.getRegon().get());
        else
            businessEntity.setRegon(null);

        return businessEntity;
    }

    public AddressEntity createAddressEntityFromDTO(AddressEntity addressEntity,BusinessDTO dto){
        addressEntity.setLocality(dto.getLocality());
        addressEntity.setPostalCode(dto.getPostalCode());
        addressEntity.setStreet(dto.getStreet());
        addressEntity.setPropertyNumber(dto.getPropertyNumber());
        addressEntity.setApartmentNumber(dto.getApartmentNumber());
        return addressEntity;
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
