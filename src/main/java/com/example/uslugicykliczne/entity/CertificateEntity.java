package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Certificate", schema = "uslugi_cykliczne", catalog = "")
public class CertificateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idCertificate")
    private int idCertificate;
    @Basic
    @Column(name = "certificateSerialNumber",nullable = false, length = 40)
    private String certificateSerialNumber;
    @Basic
    @Column(name = "validFrom",nullable = false)
    private LocalDateTime validFrom;
    @Basic
    @Column(name = "validTo",nullable = false)
    private LocalDateTime validTo;
    @Basic
    @Column(name = "cardType",nullable = false, length = 40)
    private String cardType;
    @Basic
    @Column(name = "cardNumber",nullable = false, length = 40)
    private String cardNumber;
    @Basic
    @Column(name = "nameInOrganisation", length = 80)
    private String nameInOrganisation;
    @ManyToOne
    @JoinColumn(name = "idCyclicalService",nullable = false)
    private CyclicalServiceEntity cyclicalServiceEntity;
//    @Basic
//    @Column(nullable = false)
//    private Boolean recent;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateEntity that = (CertificateEntity) o;

        if (idCertificate != that.idCertificate) return false;
        if (certificateSerialNumber != null ? !certificateSerialNumber.equals(that.certificateSerialNumber) : that.certificateSerialNumber != null)
            return false;
        if (validFrom != null ? !validFrom.equals(that.validFrom) : that.validFrom != null) return false;
        if (validTo != null ? !validTo.equals(that.validTo) : that.validTo != null) return false;
        if (cardType != null ? !cardType.equals(that.cardType) : that.cardType != null) return false;
        if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null) return false;
        if (nameInOrganisation != null ? !nameInOrganisation.equals(that.nameInOrganisation) : that.nameInOrganisation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idCertificate;
        result = 31 * result + (certificateSerialNumber != null ? certificateSerialNumber.hashCode() : 0);
        result = 31 * result + (validFrom != null ? validFrom.hashCode() : 0);
        result = 31 * result + (validTo != null ? validTo.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (nameInOrganisation != null ? nameInOrganisation.hashCode() : 0);
        return result;
    }
}
