package com.example.uslugicykliczne.dataTypes.projections;

import lombok.Data;

import java.time.LocalDateTime;

public record StatusChangeRecordProjection(Integer idStatusChange, String comment, LocalDateTime changeDate,
                                           Integer statusTypeName) {

}
