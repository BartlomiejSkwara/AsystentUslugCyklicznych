package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvAccountData {

    @CsvBindByName(column = "id")
    private Integer id;

    @CsvBindByName(column = "username")
    private String username;

    @CsvBindByName(column = "role")
    private String role;

    @CsvBindByName(column = "hashedPassword")
    private String hashedPassword;

}
