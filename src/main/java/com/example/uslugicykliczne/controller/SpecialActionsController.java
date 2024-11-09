package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.services.StartupService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialActions")
@AllArgsConstructor
public class SpecialActionsController {

    EntityManager entityManager;
    StartupService startupService;

    @PostMapping("/dbWipe")
    @Transactional
    public ResponseEntity<?> dbWipe() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0").executeUpdate();

        entityManager.createNativeQuery("DELETE FROM account_data ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM email ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM phone_number ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM certificate ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM cyclical_service ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM business ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM service_user ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM contact_data ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM status_change ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM status_type ").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM address ").executeUpdate();


        entityManager.createNativeQuery("ALTER TABLE account_data AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE email AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE phone_number AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE certificate AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE cyclical_service AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE business AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE service_user AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE contact_data AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE status_change AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE status_type AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE address AUTO_INCREMENT = 1").executeUpdate();


        startupService.start();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1").executeUpdate();
        return ResponseEntity.ok().body("Z powodzeniem wyczyszczono bazę danych");
    }
}
