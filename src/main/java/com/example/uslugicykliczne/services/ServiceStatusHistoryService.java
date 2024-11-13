package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.StatusEnum;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.StatusChangeEntity;
import com.example.uslugicykliczne.entity.StatusTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ServiceStatusHistoryService {

    @PersistenceContext()
    private  EntityManager entityManager;
    @Transactional
    public void addNewStatusHistoryRecord(LocalDateTime date, StatusEnum statusEnum,String comment,Integer cyclicalServiceId){
        StatusChangeEntity statusChangeEntity = new StatusChangeEntity();
        statusChangeEntity.setChangeDate(date==null?LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES):date);
        statusChangeEntity.setStatusTypeEntity(entityManager.getReference(StatusTypeEntity.class,statusEnum.getMaskValue()));
        statusChangeEntity.setComment(comment);
        statusChangeEntity.setCyclicalService(entityManager.getReference(CyclicalServiceEntity.class,cyclicalServiceId));
        entityManager.persist(statusChangeEntity);
    }

    @Transactional
    public void addNewStatusHistoryRecord(LocalDateTime date, Integer statusEnum,String comment,Integer cyclicalServiceId){
        StatusChangeEntity statusChangeEntity = new StatusChangeEntity();
        statusChangeEntity.setChangeDate(date==null?LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES):date);
        statusChangeEntity.setStatusTypeEntity(entityManager.getReference(StatusTypeEntity.class,statusEnum));
        statusChangeEntity.setComment(comment);
        statusChangeEntity.setCyclicalService(entityManager.getReference(CyclicalServiceEntity.class,cyclicalServiceId));
        entityManager.persist(statusChangeEntity);
    }
}
