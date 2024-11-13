package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "CyclicalService", schema = "uslugi_cykliczne", catalog = "")
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public class CyclicalServiceEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCyclicalService")
    private int idCyclicalService;

    @Basic
    @Column(name = "oneTime",nullable = false)
    private boolean oneTime;

    @Basic
    @Column(name = "agreementNumber",nullable = false, length = 40)
    private String agreementNumber;



    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private BusinessEntity business;

    @ManyToOne(optional = false)
    private ServiceUserEntity serviceUser;

    @OneToMany(mappedBy = "cyclicalServiceEntity",cascade = CascadeType.ALL)
    private List<CertificateEntity> certificates;

    @Basic
    @Column(name = "status")
    private int statusBitmap;

    @Basic
    @Column(name = "ignoreTo",nullable = true)
    private LocalDateTime ignoreTo;

}
