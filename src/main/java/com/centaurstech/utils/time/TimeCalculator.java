package com.centaurstech.utils.time;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Feliciano on 9/9/2018.
 */
public class TimeCalculator {

    public static long nowInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long nowInSeconds() {
        return Calendar.getInstance().getTimeInMillis() / 1000L;
    }

    public static String getTimeString(Date date, String zoneId) {
        return date.toInstant()
                .atZone( ZoneId.of(zoneId) )
                .format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
    }

    public static String getTimeString(Date date) {
        return getTimeString(date, "Asia/Shanghai");
    }

}
