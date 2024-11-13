package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvPhoneNumber {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "number")
    private String number;
    @CsvBindByName(column = "contact_data_id")
    private Integer cdID;
}
