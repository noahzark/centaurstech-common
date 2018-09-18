package com.centaurstech.utils.time;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Feliciano on 9/9/2018.
 */
public enum DatePeriod implements ComparablePeriod {

    DAY,
    WEEK,
    MONTH;

    public static Calendar beginOfDay(int day){
        final Calendar cal = beginOfToday();
        cal.add(Calendar.DATE, day);
        return cal;
    }

    public static Calendar endOfDay(int day){
        final Calendar cal = beginOfDay(day + 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal;
    }

    public static Calendar beginOfToday(){
//        final Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return beginOfDay(0);
    }

    public static Calendar endOfToday(){
        return endOfDay(0);
    }

    public static Calendar beginOfYesterday() {
        return beginOfDay(-1);
    }

    public static Calendar beginOfLast7Days(){
        return beginOfDay(-7);
    }

    public static Calendar beginOfLast30Days(){
        return beginOfDay(-30);
    }

    @Override
    public Boolean inPeriod(Calendar cal) {
        switch (this) {
            case DAY:
                return cal.after(beginOfYesterday()) && cal.before(endOfToday());
            case WEEK:
                return cal.after(beginOfDay(-7)) && cal.before(endOfDay(7));
            case MONTH:
                return cal.after(beginOfDay(-30)) && cal.before(endOfDay(30));
        }
        return null;
    }
}
