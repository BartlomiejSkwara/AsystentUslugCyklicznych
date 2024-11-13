package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Data
@Table(name = "Business", schema = "uslugi_cykliczne", catalog = "")
@NoArgsConstructor
@SuperBuilder
public class BusinessEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idBusiness")
    private int idBusiness;

    @Basic
    @Column(name = "name",nullable = false, length = 80)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private AddressEntity address;


    @Basic
    @Column(name = "regon", length = 40, nullable = true)
    private String regon;
    @Basic
    @Column(name = "nip",nullable = false, length = 40)
    private String nip;
    @Basic
    @Column(name = "comments")
    private String comments;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "contactDataId", referencedColumnName = "idContactData", nullable = false)
    private ContactDataEntity contactData;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessEntity that)) return false;
        return getIdBusiness() == that.getIdBusiness() && Objects.equals(getName(), that.getName()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getRegon(), that.getRegon()) && Objects.equals(getNip(), that.getNip()) && Objects.equals(getComments(), that.getComments()) && Objects.equals(getContactData(), that.getContactData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdBusiness(), getName(), getAddress(), getRegon(), getNip(), getComments(), getContactData());
    }
}
