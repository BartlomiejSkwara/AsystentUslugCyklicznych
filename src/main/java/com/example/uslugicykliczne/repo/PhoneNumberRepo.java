package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface PhoneNumberRepo  extends ListCrudRepository<PhoneNumberEntity,Integer> {
}
