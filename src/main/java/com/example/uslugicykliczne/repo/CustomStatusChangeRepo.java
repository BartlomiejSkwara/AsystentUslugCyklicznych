package com.example.uslugicykliczne.repo;


import com.example.uslugicykliczne.dataTypes.projections.StatusChangeRecordProjection;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.StatusChangeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

interface CustomStatusChangeRepo {
    List<StatusChangeRecordProjection> findByServiceIdWithChronologicalOrder(Integer id);
    @Transactional
    void deleteByCyclicalService(Integer id);
}


@RequiredArgsConstructor
class  CustomStatusChangeRepoImpl implements  CustomStatusChangeRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StatusChangeRecordProjection> findByServiceIdWithChronologicalOrder(Integer id) {
        Query query = entityManager.createQuery(
                "select new com.example.uslugicykliczne.dataTypes.projections.StatusChangeRecordProjection(sce.idStatusChange, sce.comment,sce.changeDate, sce.statusTypeEntity.id) " +
                        "from  StatusChangeEntity sce where sce.cyclicalService.idCyclicalService=:serId order by sce.changeDate asc "
        );
        query.setParameter("serId",id);

        List<StatusChangeRecordProjection> resultList = query.getResultList();

        return resultList;
    }

    @Override
    @Transactional
    public void deleteByCyclicalService(Integer id) {
        Query query = entityManager.createQuery(
                "DELETE  StatusChangeEntity  where cyclicalService.idCyclicalService = :ser "
        );
        query.setParameter("ser",id);
        query.executeUpdate();

    }
}
