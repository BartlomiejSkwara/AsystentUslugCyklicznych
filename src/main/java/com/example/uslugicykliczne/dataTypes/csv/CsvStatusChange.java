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
public class CsvStatusChange {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "comment")
    private String comment;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm")
    @CsvBindByName(column = "changeDate")
    private LocalDateTime changeDate;
    @CsvBindByName(column = "status_type")
    private Integer status_type;
    @CsvBindByName(column = "cyclical_service_id")
    private Integer cyclical_service_id;

}
