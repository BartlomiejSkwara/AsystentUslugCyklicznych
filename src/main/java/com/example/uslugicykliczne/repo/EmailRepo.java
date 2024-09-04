package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.EmailEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface EmailRepo  extends ListCrudRepository<EmailEntity,Integer> {
}
