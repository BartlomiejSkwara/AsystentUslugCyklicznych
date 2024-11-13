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
public class CsvCertificate {
    @CsvBindByName(column = "id")
    private Integer id;
    @CsvBindByName(column = "serial_number")
    private String serialNumber;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm")
    @CsvBindByName(column = "valid_from")
    private LocalDateTime validFrom;

    @CsvDate(value = "yyyy-MM-dd'T'HH:mm")
    @CsvBindByName(column = "valid_to")
    private LocalDateTime validTo;

    @CsvBindByName(column = "card_type")
    private Integer cardType;
    @CsvBindByName(column = "card_number")
    private String cardNumber;
    @CsvBindByName(column = "name_in_org")
    private String nameInOrg;
    @CsvBindByName(column = "most_recent")
    private boolean mostRecent;
    @CsvBindByName(column = "cyclical_service_id")
    private Integer csID;
}
