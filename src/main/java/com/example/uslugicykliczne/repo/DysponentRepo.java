package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.entity.DysponentEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface DysponentRepo extends ListCrudRepository<DysponentEntity,Integer> {

    List<DysponentEntity> findDysponentEntitiesByEmailOrPhoneNumberOrMfnSerialNumber(String email, String phoneNumber, String mfnSerialNumber);

}
