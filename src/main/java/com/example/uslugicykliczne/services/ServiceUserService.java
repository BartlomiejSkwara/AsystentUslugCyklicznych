package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.ServiceUserDTO;
import com.example.uslugicykliczne.entity.*;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@Service
public class ServiceUserService {
    private final ServiceUserRepo serviceUserRepo;
    private final AccountManagementService accountManagementService;
    private final ContactDataService contactDataService;
    private final PasswordEncoder passwordEncoder;

    public ServiceUserService(ServiceUserRepo serviceUserRepo, AccountManagementService accountManagementService, ContactDataService contactDataService, PasswordEncoder passwordEncoder) {
        this.serviceUserRepo = serviceUserRepo;
        this.accountManagementService = accountManagementService;
        this.contactDataService = contactDataService;
        this.passwordEncoder = passwordEncoder;
    }



    public ResponseEntity<String> updateServiceUserEntity(Integer id, ServiceUserDTO serviceUserDTO,boolean duplicateCheck){
        Optional<ServiceUserEntity> serviceUserEntity = serviceUserRepo.findUserWithContactDataById(id);
        if (serviceUserEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent service user !!!");

        ServiceUserEntity user = serviceUserEntity.get();

        ContactDataEntity contactDataEntity = serviceUserEntity.get().getContactData();

        if(duplicateCheck){
            HashSet<String> foundDuplicates = contactDataService.findContactsInDB(serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());
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

        contactDataService.updateContactDataEntity(contactDataEntity,serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());


        if (!user.getAccountDataEntity().getUsername().equals(serviceUserDTO.getLogin())) {
            user.getAccountDataEntity().setUsername(serviceUserDTO.getLogin());
        }


        if (serviceUserDTO.getPassword() != null && !serviceUserDTO.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(serviceUserDTO.getPassword());
            user.getAccountDataEntity().setHashedPassword(encodedPassword);
        }

        serviceUserRepo.save(createServiceUserEntityFromDTO(serviceUserEntity.get(), serviceUserDTO,contactDataEntity));
        return ResponseEntity.ok("Successfully updated the user");
    }

    @Transactional
    public ResponseEntity<String> insertNewServiceUserEntity(ServiceUserDTO serviceUserDTO,boolean duplicateCheck){


        //List<ServiceUserEntity> duplicateUniques = customerRepo.findCustomerEntitiesByEmailOrPhoneNumber(customerDto.getEmail(), customerDto.getPhoneNumber());
        //if(duplicateUniques.isEmpty()){
            if(duplicateCheck){
                HashSet<String> foundDuplicates = contactDataService.findContactsInDB(serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());
                if(!foundDuplicates.isEmpty()){
                    return ResponseEntity.badRequest().body("Podano zarejestrowane już dane kontaktowe: "+foundDuplicates);
                }
            }


            AccountDataEntity accountDataEntity = accountManagementService.register(serviceUserDTO.getPassword(), serviceUserDTO.getLogin());
            if(accountDataEntity==null){
                return ResponseEntity.badRequest().body("Użytkownik o takiej nazwie już istnieje !!! ");
            }

            ContactDataEntity contactDataEntity = contactDataService.insertContactDataEntity(serviceUserDTO.getEmails(),serviceUserDTO.getPhoneNumbers());
            ServiceUserEntity sue = createServiceUserEntityFromDTO(new ServiceUserEntity(),serviceUserDTO,contactDataEntity);


            sue.setAccountDataEntity(accountDataEntity);

            serviceUserRepo.save(sue);
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
    public ServiceUserEntity createServiceUserEntityFromDTO (ServiceUserEntity serviceUserEntity, ServiceUserDTO dto, ContactDataEntity contactDataEntity){
        serviceUserEntity.setName(dto.getName().trim());
        serviceUserEntity.setSurname(dto.getSurname().trim());
        serviceUserEntity.setComments(dto.getComments());
        serviceUserEntity.setHasPolishPesel(dto.getHasPolishPESEL());
        if (dto.getHasPolishPESEL())
            if (dto.getTaxId().isPresent())
                serviceUserEntity.setTaxIdentificationNumber(dto.getTaxId().get());
            else
                serviceUserEntity.setHasPolishPesel(false);
        else
            serviceUserEntity.setTaxIdentificationNumber(null);

        serviceUserEntity.setContactData(contactDataEntity);

        return serviceUserEntity;
    }

    public void deleteServiceUser(Integer id) {
        Optional<ServiceUserEntity> serviceUserEntity = serviceUserRepo.findById(id);
        if (serviceUserEntity.isPresent()){
            serviceUserRepo.delete(serviceUserEntity.get());
        }


    }
}
