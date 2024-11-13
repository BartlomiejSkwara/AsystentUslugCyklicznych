package com.example.uslugicykliczne.dataTypes.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvCyclicalService {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "onTime")
    private boolean onTime;
    @CsvBindByName(column = "agreementNumber")
    private String agreementNumber;
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "business_id")
    private Integer bID;
    @CsvBindByName(column = "serviceUser_id")
    private Integer seID;
    @CsvBindByName(column = "status_bitmask")
    private int sBitmask;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm")
    @CsvBindByName(column = "ignoreTo")
    private LocalDateTime ignoreTo;
}
