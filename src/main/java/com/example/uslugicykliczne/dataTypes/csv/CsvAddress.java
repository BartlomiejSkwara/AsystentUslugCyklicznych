package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvAddress {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "postalCode")
    private String postalCode;
    @CsvBindByName(column = "locality")
    private String locality;
    @CsvBindByName(column = "street")
    private String street;
    @CsvBindByName(column = "propertyNumber")
    private Integer propertyNumber;
    @CsvBindByName(column = "apartmentNumber")
    private Integer apartmentNumber;
}
