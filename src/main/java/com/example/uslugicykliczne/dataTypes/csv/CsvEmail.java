package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvEmail {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "email")
    private String email;
    @CsvBindByName(column = "contact_data_id")
    private Integer cdID;
}
