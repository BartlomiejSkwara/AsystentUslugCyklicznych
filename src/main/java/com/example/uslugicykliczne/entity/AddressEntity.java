package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "Address", schema = "uslugi_cykliczne", catalog = "")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @Basic
    @Column(name = "postalCode",nullable = false, length = 20)
    private String postalCode;
    @Basic
    @Column(name = "locality", nullable = false, length = 40)
    private String locality;
    @Basic
    @Column(name="street", nullable = false,length = 40)
    private String street;
    @Basic
    @Column(name="propertyNumber", nullable = true)
    private Integer propertyNumber;
    @Basic
    @Column(name="apartmentNumber", nullable = true )
    private Integer apartmentNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEntity that)) return false;
        return getAddressId() == that.getAddressId() && Objects.equals(getPostalCode(), that.getPostalCode()) && Objects.equals(getLocality(), that.getLocality()) && Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getPropertyNumber(), that.getPropertyNumber()) && Objects.equals(getApartmentNumber(), that.getApartmentNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddressId(), getPostalCode(), getLocality(), getStreet(), getStreet(), getPropertyNumber(), getApartmentNumber());
    }
}
