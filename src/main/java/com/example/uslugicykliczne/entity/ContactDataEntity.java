package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "ContactData", schema = "uslugi_cykliczne", catalog = "")
public class ContactDataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idContactData")
    private int idContactData;
    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    private Collection<EmailEntity> emails;
    //contactDataByContactDataIdContactData

    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    private Collection<PhoneNumberEntity> phoneNumbers;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactDataEntity that = (ContactDataEntity) o;

        if (idContactData != that.idContactData) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return idContactData;
    }


}
