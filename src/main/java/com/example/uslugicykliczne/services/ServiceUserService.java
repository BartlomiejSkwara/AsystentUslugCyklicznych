package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.ServiceUserDTO;
import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ServiceUserService {
    private final ServiceUserRepo serviceUserRepo;
    private final  ContactDataService contactDataService;
    public ServiceUserService(ServiceUserRepo serviceUserRepo, ContactDataService contactDataService) {
        this.serviceUserRepo = serviceUserRepo;

        this.contactDataService = contactDataService;
    }



    public ResponseEntity<String> updateCustomerEntity(Integer id,ServiceUserDTO serviceUserDTO){
        Optional<ServiceUserEntity> serviceUserEntity = serviceUserRepo.findById(id);
        if (serviceUserEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistant service user !!!");
        ContactDataEntity contactDataEntity = serviceUserEntity.get().getContactData();
        contactDataService.updateContactDataEntity(contactDataEntity,serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());

        serviceUserRepo.save(createServiceUserEntityFromDTO(serviceUserEntity.get(), serviceUserDTO,contactDataEntity));
        return ResponseEntity.ok("Successfully updated the user");
    }

    @Transactional
    public ResponseEntity<String> insertNewServiceUserEntity(ServiceUserDTO serviceUserDTO){
        //List<ServiceUserEntity> duplicateUniques = customerRepo.findCustomerEntitiesByEmailOrPhoneNumber(customerDto.getEmail(), customerDto.getPhoneNumber());
        //if(duplicateUniques.isEmpty()){
            ContactDataEntity contactDataEntity = contactDataService.insertContactDataEntity(serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());
            serviceUserRepo.save(createServiceUserEntityFromDTO(new ServiceUserEntity(),serviceUserDTO,contactDataEntity));
            /// TODO może jakaś walidacja czy poprawnie dodano maile i numery
            return ResponseEntity.ok("Successfully added the user");
        //}
//        else {
//            StringBuilder error = new StringBuilder("There can't be Customers with duplicate:");
//
//            HashSet<String> duplicates = new HashSet<>();
//            duplicateUniques.forEach(customerEntity ->
//                {
//                    if(customerEntity.getEmail().equals(customerDto.getEmail()))
//                        duplicates.add("[email]");
//                    if(customerEntity.getPhoneNumber().equals(customerDto.getPhoneNumber()))
//                        duplicates.add("[phone number]");
//                }
//
//            );
//
//            duplicates.forEach(s ->
//                        error.append(s)
//                    );
//
//            return ResponseEntity.status(409).body(error.toString());
//        }
    }
    public ServiceUserEntity createServiceUserEntityFromDTO (ServiceUserEntity serviceUserEntity, ServiceUserDTO dto, ContactDataEntity contactDataEntity ){
        serviceUserEntity.setName(dto.getName());
        serviceUserEntity.setSurname(dto.getSurname());
        serviceUserEntity.setComments(dto.getComments());
        serviceUserEntity.setHasPolishPesel(dto.getHasPolishPESEL());
        if (dto.getHasPolishPESEL())
            if (dto.getTaxId().isPresent())
                serviceUserEntity.setTaxIdentificationNumber(dto.getTaxId().get());
            else
                serviceUserEntity.setHasPolishPesel(false);
        serviceUserEntity.setContactData(contactDataEntity);

        return serviceUserEntity;
    }

}
