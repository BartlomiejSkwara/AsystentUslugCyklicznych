package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "ServiceUser", schema = "uslugi_cykliczne", catalog = "")
public class ServiceUserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idServiceUser")
    private int idServiceUser;
    @Basic
    @Column(name = "name",nullable = false, length = 40)
    private String name;
    @Basic
    @Column(name = "surname",nullable = false, length = 40)
    private String surname;
    @Basic
    @Column(name = "comments")
    private String comments;
    @Basic
    @Column(name = "hasPolishPESEL",nullable = false)
    private boolean hasPolishPesel;
    @Basic
    @Column(name = "taxIdentificationNumber", length = 80)
    private String taxIdentificationNumber;
    @OneToMany(mappedBy = "serviceUser")
    private Collection<CyclicalServiceEntity> cyclicalServices;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contactDataId", referencedColumnName = "idContactData", nullable = false)
    private ContactDataEntity contactData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountData",referencedColumnName = "idLoginCredentials",nullable = false)
    private AccountDataEntity accountDataEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceUserEntity that = (ServiceUserEntity) o;

        if (idServiceUser != that.idServiceUser) return false;
        if (hasPolishPesel != that.hasPolishPesel) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (taxIdentificationNumber != null ? !taxIdentificationNumber.equals(that.taxIdentificationNumber) : that.taxIdentificationNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idServiceUser;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (hasPolishPesel ? 1 : 0);
        result = 31 * result + (taxIdentificationNumber != null ? taxIdentificationNumber.hashCode() : 0);
        return result;
    }

}
