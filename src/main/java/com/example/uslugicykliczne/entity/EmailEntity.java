package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@Table(name = "Email", schema = "uslugi_cykliczne", catalog = "")
@NoArgsConstructor
@SuperBuilder
public class EmailEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idEmail")
    private int idEmail;

    @ManyToOne()
    @JoinColumn(name = "idContactData",nullable = false)
    ContactDataEntity contactDataEntity;

    @Basic
    @Column(name = "email", nullable = false, length = 40)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailEntity that = (EmailEntity) o;

        if (idEmail != that.idEmail) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idEmail;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmailEntity{" +
                "idEmail=" + idEmail +
                ", email='" + email + '\'' +
                '}';
    }

}
