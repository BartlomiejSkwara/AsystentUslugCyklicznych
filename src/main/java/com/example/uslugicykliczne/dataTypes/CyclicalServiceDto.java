package com.example.uslugicykliczne.dataTypes;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

@Getter()
public class CyclicalServiceDto {
    @NotBlank(message = "Description is empty !!!")
    private final String description;


    @NotNull(message = "Price is empty !!!")
    private final Double price;

    @FutureOrPresent(message = "future or present constraint broken")
    @NotNull(message = "FirstCycleStart is empty !!!")
    private final LocalDateTime firstCycleStart;

    @NotNull(message = "RenewalPeriod is empty !!!")
    private final Period renewalPeriod;

    @NotNull(message = "DysponentId is empty !!!")
    private final Integer dysponentId;

    @NotNull(message = "CustomerId is empty !!!")
    private final Integer customerId;

    private final LocalDateTime nextRenewal;


    public CyclicalServiceDto(String description, Double price, LocalDateTime firstCycleStart, Period renewalPeriod, Integer dysponentId, Integer customerId) {
        this.description = description;
        this.price = price;
        this.firstCycleStart = firstCycleStart;
        this.renewalPeriod = renewalPeriod;
        this.dysponentId = dysponentId;
        this.customerId = customerId;
        this.nextRenewal = firstCycleStart.plus(renewalPeriod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CyclicalServiceDto that)) return false;
        return Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getFirstCycleStart(), that.getFirstCycleStart()) && Objects.equals(getRenewalPeriod(), that.getRenewalPeriod()) && Objects.equals(getDysponentId(), that.getDysponentId()) && Objects.equals(getCustomerId(), that.getCustomerId()) && Objects.equals(getNextRenewal(), that.getNextRenewal());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getPrice(), getFirstCycleStart(), getRenewalPeriod(), getDysponentId(), getCustomerId(), getNextRenewal());
    }
}
