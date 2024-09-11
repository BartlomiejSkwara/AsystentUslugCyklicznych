package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.EmailEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;

public interface ContactDataRepo  extends ListCrudRepository<ContactDataEntity,Integer> {
    //Collection<>

}
