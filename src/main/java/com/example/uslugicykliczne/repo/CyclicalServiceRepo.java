package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CyclicalServiceRepo extends ListCrudRepository<CyclicalServiceEntity,Integer> {

    @Query("SELECT e FROM CyclicalServiceEntity e WHERE e.nextRenewal < :dateLimit ORDER BY e.nextRenewal ASC LIMIT 1")
    Optional<CyclicalServiceEntity> findFirstDateBefore(@Param("dateLimit") LocalDateTime dateLimit);

    @Query("SELECT  e FROM CyclicalServiceEntity e ORDER BY e.nextRenewal ASC LIMIT  1")
    Optional<CyclicalServiceEntity> findServiceWithNextRenewalDate();
}
