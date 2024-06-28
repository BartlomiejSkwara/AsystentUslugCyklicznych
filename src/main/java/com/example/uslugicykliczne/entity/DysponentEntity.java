package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "dysponenet")
@Entity
public class DysponentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;

    @Column(unique = true)
    private String mfnSerialNumber;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

}
