package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Certificate", schema = "uslugi_cykliczne", catalog = "")
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public class CertificateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCertificate")
    private int idCertificate;
    @Basic
    @Column(name = "certificateSerialNumber",nullable = false, length = 40)
    private String certificateSerialNumber;
    @Basic
    @Column(name = "validFrom",nullable = false)
    private LocalDateTime validFrom;
    @Basic
    @Column(name = "validTo",nullable = false)
    private LocalDateTime validTo;
    @Basic
    @Column(name = "cardType",nullable = false)
    private Integer cardType;
    @Basic
    @Column(name = "cardNumber",nullable = false, length = 40)
    private String cardNumber;
    @Basic
    @Column(name = "nameInOrganisation", length = 80)
    private String nameInOrganisation;
    @Basic
    @Column(nullable = false)
    private boolean renewalMessageSent;
    @Basic
    @Column(name = "mostRecent",nullable = false)
    private boolean mostRecent;
    @ManyToOne
    @JoinColumn(name = "idCyclicalService",nullable = false)
    private CyclicalServiceEntity cyclicalServiceEntity;



}
