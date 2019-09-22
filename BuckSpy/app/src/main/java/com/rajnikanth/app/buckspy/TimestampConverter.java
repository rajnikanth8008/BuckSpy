package com.rajnikanth.app.buckspy;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {
    private static DateFormat df = new SimpleDateFormat("DD-MM-YYYY HH:MM:SS.SSS", Locale.US);

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}