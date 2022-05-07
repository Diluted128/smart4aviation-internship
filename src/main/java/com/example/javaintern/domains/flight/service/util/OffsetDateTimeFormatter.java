package com.example.javaintern.domains.flight.service.util;

import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class OffsetDateTimeFormatter {

    static String offset = "+00:00";

    public static OffsetDateTime parse(String date){
        OffsetDateTime offsetDateTime;
        try{
           offsetDateTime = OffsetDateTime.parse(date)
                                      .withOffsetSameInstant(ZoneOffset.of(offset));
        }
        catch (Exception e){
            throw new DateTimeException("Date format is invalid");
        }
        return offsetDateTime;
    }

    public static OffsetDateTime alterTimezone(OffsetDateTime offsetDateTime){
         return offsetDateTime.withOffsetSameInstant(ZoneOffset.of(offset));
    }
}
