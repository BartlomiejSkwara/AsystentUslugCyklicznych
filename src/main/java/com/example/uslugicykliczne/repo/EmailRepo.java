package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.EmailEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface EmailRepo  extends ListCrudRepository<EmailEntity,Integer> {

    @Query("delete EmailEntity s where s in (:deleted)")
    void deleteAllBy(List<EmailEntity> deleted);

    @Query("select EmailEntity FROM EmailEntity mail where mail.email in (:emailList)")
    List<EmailEntity> findAllLike(List<String> emailList);

}
