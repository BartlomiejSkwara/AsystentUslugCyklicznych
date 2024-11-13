package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvServiceUser {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "surname")
    private String surname;
    @CsvBindByName(column = "comments")
    private String comments;
    @CsvBindByName(column = "hasPolishPesel")
    private boolean hasPolishPesel;
    @CsvBindByName(column = "taxID")
    private String taxID;
    @CsvBindByName(column = "contact_data_id")
    private Integer cdId;
    @CsvBindByName(column = "account_id")
    private Integer aID;
}
