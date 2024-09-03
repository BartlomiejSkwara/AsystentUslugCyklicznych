package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Business", schema = "uslugi_cykliczne", catalog = "")
public class BusinessEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idBusiness")
    private int idBusiness;
    @Basic
    @Column(name = "name",nullable = false, length = 80)
    private String name;
    @Basic
    @Column(name = "adres",nullable = false, length = 80)
    private String adres;
    @Basic
    @Column(name = "regon",nullable = false, length = 40)
    private String regon;
    @Basic
    @Column(name = "nip",nullable = false, length = 40)
    private String nip;
    @Basic
    @Column(name = "comments")
    private String comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ContactData_idContactData", referencedColumnName = "idContactData", nullable = false)
    private ContactDataEntity contactDataByContactDataIdContactData;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessEntity that = (BusinessEntity) o;

        if (idBusiness != that.idBusiness) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (adres != null ? !adres.equals(that.adres) : that.adres != null) return false;
        if (regon != null ? !regon.equals(that.regon) : that.regon != null) return false;
        if (nip != null ? !nip.equals(that.nip) : that.nip != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idBusiness;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (adres != null ? adres.hashCode() : 0);
        result = 31 * result + (regon != null ? regon.hashCode() : 0);
        result = 31 * result + (nip != null ? nip.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

}
