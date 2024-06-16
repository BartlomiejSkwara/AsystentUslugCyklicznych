package com.example.uslugicykliczne.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Period;

@Converter
public class PeriodConverter implements AttributeConverter<Period,String> {
    private static final String SEPARATOR = ", ";
    @Override
    public String convertToDatabaseColumn(Period period) {
        if (period==null)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(period.getYears());
        sb.append(SEPARATOR);
        sb.append(period.getMonths());
        sb.append(SEPARATOR);
        sb.append(period.getDays());

        return sb.toString();
    }


    @Override
    public Period convertToEntityAttribute(String dbPeriod) {
        if(dbPeriod==null||dbPeriod.isEmpty())
            return null;

        String[] pieces  = dbPeriod.split(SEPARATOR);

        if(pieces==null||pieces.length==0)
            return null;

        int year = 1;
        int month = 0;
        int day = 0;

        try {
            year = Integer.parseInt(pieces[0]);
            month = Integer.parseInt(pieces[1]);
            day = Integer.parseInt(pieces[2]);
        }catch (Exception e){

        }

        return Period.of(year,month,day);

    }
}
