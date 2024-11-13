package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvBusiness {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "addres_id")
    private Integer aID;
    @CsvBindByName(column = "regon")
    private String regon;
    @CsvBindByName(column = "nip")
    private String nip;
    @CsvBindByName(column = "comments")
    private String comments;
    @CsvBindByName(column = "contact_data_id")
    private Integer cdId;
}
