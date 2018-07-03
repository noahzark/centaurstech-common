package com.centaurstech.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Feliciano on 2/7/2018.
 */
public class TimeUtil {

    public enum Period {
        DAY,
        WEEK,
        MONTH
    }

    public static Date beginOfToday(){
        final Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date beginOfDay(int day){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(TimeUtil.beginOfToday());
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date endOfDay(int day){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(TimeUtil.beginOfDay(day + 1));
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public static Date beginOfYesterday() {
        return TimeUtil.beginOfDay(-1);
    }

    public static Date endOfToday(){
        return TimeUtil.endOfDay(0);
    }

    public static Date beginOfLast7Days(){
        return TimeUtil.beginOfDay(-7);
    }

    public static Date beginOfLast30Days(){
        return TimeUtil.beginOfDay(-30);
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
