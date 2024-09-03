package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountData", schema = "uslugi_cykliczne", catalog = "")
public class AccountDataEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idLoginCredentials")
    private int idLoginCredentials;
    @Basic
    @Column(name = "username",nullable = false, length = 40)
    private String username;
    @Basic
    @Column(name = "hashedPassword",nullable = false) /// TODO ustal maks dlugosc gdy bedziesz wiedział jaki algorytm hashowania uzyjesz
    private String hashedPassword;
    @Basic
    @Column(name = "role",nullable = false, length = 40)
    private String role;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDataEntity that = (AccountDataEntity) o;

        if (idLoginCredentials != that.idLoginCredentials) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (hashedPassword != null ? !hashedPassword.equals(that.hashedPassword) : that.hashedPassword != null)
            return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idLoginCredentials;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (hashedPassword != null ? hashedPassword.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
