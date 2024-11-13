package com.example.uslugicykliczne.utility;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeUtility {
    static private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    static public ZoneOffset getZoneOffset(){
        return zoneOffset;
    }
    public static Date getCurrentDate(){
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return Date.from(localDateTime.toInstant(zoneOffset));
    }

    static public boolean isSameDay(Instant instant1, Instant instant2){
        if(instant1==null || instant2 == null){
            return false;
        }
        return instant1.truncatedTo(ChronoUnit.DAYS).equals(instant2.truncatedTo(ChronoUnit.DAYS));
    }

    static public boolean isToday(Instant instant1){
        return isSameDay(instant1, LocalDateTime.now().toInstant(getZoneOffset()));
    }
    static public Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(getZoneOffset());
    }
}
