package com.example.uslugicykliczne.entity;


import com.example.uslugicykliczne.UslugiCykliczneApplication;
import com.example.uslugicykliczne.repo.CustomerRepo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customer")
@Entity
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "customerEntity", cascade = CascadeType.ALL)
    private List<CyclicalServiceEntity> orders = new ArrayList<>();



}

