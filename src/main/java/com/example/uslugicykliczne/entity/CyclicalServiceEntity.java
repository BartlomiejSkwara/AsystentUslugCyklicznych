package com.example.uslugicykliczne.entity;

import com.example.uslugicykliczne.converters.PeriodConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;


@NoArgsConstructor
@Data
@Table(name = "cyclicalService")
@Entity
public class CyclicalServiceEntity {

    public CyclicalServiceEntity(String name, String surname, LocalDateTime firstCycleStart, Period period) {
        this.name = name;
        this.surname = surname;
        this.firstCycleStart = firstCycleStart;
        this.renewalPeriod = period;
        this.nextRenewal = firstCycleStart.plus(period);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;

    @Basic
    private LocalDateTime firstCycleStart;
    @Basic
    private LocalDateTime nextRenewal;

    @Convert(converter = PeriodConverter.class)
    private Period renewalPeriod;

    @ManyToOne
    private DysponentEntity dysponentEntity;

    @ManyToOne
    private CustomerEntity customerEntity;


}
