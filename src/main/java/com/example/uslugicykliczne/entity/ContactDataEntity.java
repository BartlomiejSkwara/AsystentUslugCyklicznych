package com.example.uslugicykliczne.entity;

import com.example.uslugicykliczne.services.PhoneNumberService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
@Table(name = "ContactData", schema = "uslugi_cykliczne", catalog = "")
public class ContactDataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idContactData")
    private int idContactData;
    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL)
    private List<EmailEntity> emails;

    @OneToMany(mappedBy = "contactDataEntity", cascade = CascadeType.ALL)
    private List<PhoneNumberEntity> phoneNumbers;




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
