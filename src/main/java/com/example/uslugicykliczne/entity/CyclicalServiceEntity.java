package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "CyclicalService", schema = "uslugi_cykliczne", catalog = "")
@EqualsAndHashCode
public class CyclicalServiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCyclicalService")
    private int idCyclicalService;
    @Basic
    @Column(name = "price",nullable = false)
    private double price;
    @Basic
    @Column(name = "oneTime",nullable = false)
    private boolean oneTime;
    @Basic
    @Column(name = "agreementNumber",nullable = false, length = 40)
    private String agreementNumber;
//    @Basic
//    @Column(name = "signatureType",nullable = false)
//    private int signatureType;
    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne()
    private BusinessEntity business;

    @ManyToOne(optional = false)
    private ServiceUserEntity serviceUser;

    @OneToMany(mappedBy = "cyclicalServiceEntity",cascade = CascadeType.ALL)
    private List<CertificateEntity> certificates;

    @Basic
    @Column(name = "status")
    private int statusBitmap;

    @ManyToOne()
    private AccountDataEntity assignedAccountDataEntity;

}
