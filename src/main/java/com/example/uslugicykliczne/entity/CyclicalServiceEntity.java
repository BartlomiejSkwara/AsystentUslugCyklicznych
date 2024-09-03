package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;

@Entity
@Data
@Table(name = "CyclicalService", schema = "uslugi_cykliczne", catalog = "")
public class CyclicalServiceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCyclicalService")
    private int idCyclicalService;
    @Basic
    @Column(name = "price",nullable = false)
    private double price;
    @Basic
    @Column(name = "renewalMessageSent",nullable = false)
    private boolean renewalMessageSent;
    @Basic
    @Column(name = "oneTime",nullable = false)
    private boolean oneTime;
    @Basic
    @Column(name = "agreementNumber",nullable = false, length = 40)
    private String agreementNumber;
    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne
    //@JoinColumns({@JoinColumn(name = "Business_idBusiness", referencedColumnName = "idBusiness"), @JoinColumn(name = "Business_ContactData_idContactData", referencedColumnName = "ContactData_idContactData")})
    private BusinessEntity business;

    @ManyToOne(optional = false)
    //@JoinColumns({@JoinColumn(name = "ServiceUser_idServiceUser", referencedColumnName = "idServiceUser", nullable = false), @JoinColumn(name = "ServiceUser_ContactData_idContactData", referencedColumnName = "ContactData_idContactData", nullable = false)})
    private ServiceUserEntity serviceUser;

    @OneToMany(mappedBy = "idCertificate")
    private Collection<CertificateEntity> certificateEntity;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CyclicalServiceEntity that = (CyclicalServiceEntity) o;

        if (idCyclicalService != that.idCyclicalService) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (renewalMessageSent != that.renewalMessageSent) return false;
        if (oneTime != that.oneTime) return false;
        if (agreementNumber != null ? !agreementNumber.equals(that.agreementNumber) : that.agreementNumber != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idCyclicalService;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (renewalMessageSent ? 1 : 0);
        result = 31 * result + (oneTime ? 1 : 0);
        result = 31 * result + (agreementNumber != null ? agreementNumber.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
