package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "PhoneNumber", schema = "uslugi_cykliczne", catalog = "")
public class PhoneNumberEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idPhoneNumber")
    private int idPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "idContactData",nullable = false)
    ContactDataEntity contactDataEntity;

    @Basic
    @Column(name = "number", length = 40,nullable = false)
    private String number;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneNumberEntity that = (PhoneNumberEntity) o;

        if (idPhoneNumber != that.idPhoneNumber) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idPhoneNumber;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhoneNumberEntity{" +
                "idPhoneNumber=" + idPhoneNumber +
                ", number='" + number + '\'' +
                '}';
    }


}
