package com.example.uslugicykliczne.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Data
@Table(name = "StatusType", schema = "uslugi_cykliczne", catalog = "")
@NoArgsConstructor
@SuperBuilder
public class StatusTypeEntity {
    @Id
    @Column(name = "idStatusType")
    private int idStatusType;

    @Basic
    @Column(name = "statusName",nullable = false,length = 40)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusTypeEntity that)) return false;
        return getIdStatusType() == that.getIdStatusType() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdStatusType(), getName());
    }
}
