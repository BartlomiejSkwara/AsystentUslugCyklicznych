package com.example.uslugicykliczne.entity;

import com.example.uslugicykliczne.services.PhoneNumberService;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Entity
@Data
@Table(name = "ContactData", schema = "uslugi_cykliczne", catalog = "")
public class ContactDataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idContactData")
    private int idContactData;
    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    private List<EmailEntity> emails;
    //contactDataByContactDataIdContactData

    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    private List<PhoneNumberEntity> phoneNumbers;


    @OneToOne(mappedBy = "contactData")
    private ServiceUserEntity serviceUserEntity;

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

    @Override
    public String toString() {
        return "ContactDataEntity{" +
                "idContactData=" + idContactData +
                ", emails=" + emails +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }

    public void removeEmails(List<EmailEntity> emailEntities) {
        emails.removeAll(emailEntities);

    }
    public void removePhoneNumber(List<PhoneNumberEntity> phoneNumberEntities) {
        phoneNumbers.removeAll(phoneNumberEntities);

    }
}
