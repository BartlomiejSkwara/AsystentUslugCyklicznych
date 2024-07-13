package com.example.uslugicykliczne.utility;

import org.springframework.stereotype.Service;

import java.time.ZoneOffset;

public class TimeUtility {
    static private final ZoneOffset zoneOffset = ZoneOffset.UTC;
    static public ZoneOffset getZoneOffset(){
        return zoneOffset;
    }
}
