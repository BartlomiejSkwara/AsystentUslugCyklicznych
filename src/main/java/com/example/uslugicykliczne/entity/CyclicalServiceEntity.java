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
    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne()
    //@JoinColumns({@JoinColumn(name = "Business_idBusiness", referencedColumnName = "idBusiness"), @JoinColumn(name = "Business_ContactData_idContactData", referencedColumnName = "ContactData_idContactData")})
    private BusinessEntity business;

    @ManyToOne(optional = false)
    //@JoinColumns({@JoinColumn(name = "ServiceUser_idServiceUser", referencedColumnName = "idServiceUser", nullable = false), @JoinColumn(name = "ServiceUser_ContactData_idContactData", referencedColumnName = "ContactData_idContactData", nullable = false)})
    private ServiceUserEntity serviceUser;

    @OneToMany(mappedBy = "cyclicalServiceEntity",cascade = CascadeType.ALL)
    private List<CertificateEntity> certificates;




}
