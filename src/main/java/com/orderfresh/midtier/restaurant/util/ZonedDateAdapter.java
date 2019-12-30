package com.orderfresh.midtier.restaurant.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ZonedDateAdapter extends XmlAdapter<String, ZonedDateTime> {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private static DateTimeFormatter dateTimeFormatterWithoutSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public ZonedDateTime unmarshal(String v) throws Exception {

        if (v == null) {
            return null;
        }
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(v, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            dateTime = LocalDateTime.parse(v, dateTimeFormatterWithoutSeconds);
        }
        return dateTime.atZone(ZoneId.of("UTC"));
    }

    @Override
    public String marshal(ZonedDateTime v) throws Exception {
        return v.toString();
    }
}