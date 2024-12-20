package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.entity.PhoneNumberEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PhoneNumberRepo  extends ListCrudRepository<PhoneNumberEntity,Integer> {

    @Query("select pn.number FROM  PhoneNumberEntity pn where pn.number in (:numberList)")
    List<String> findAllLike(List<String> numberList);
}
