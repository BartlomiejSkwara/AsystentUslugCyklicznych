package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "StatusChange", schema = "uslugi_cykliczne", catalog = "")
@NoArgsConstructor
@SuperBuilder
public class StatusChangeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idStatusChange")
    private int idStatusChange;

    @Basic
    @Column(name = "comment",length = 255)
    private String comment;

    @Basic
    @Column(name = "changeDate",nullable = false)
    private LocalDateTime changeDate;

    @ManyToOne
    @JoinColumn(name = "idStatusType",nullable = false)
    private StatusTypeEntity statusTypeEntity;


    @ManyToOne()
    @JoinColumn(name = "idCyclicalService", nullable = true)
    private CyclicalServiceEntity cyclicalService;
}
