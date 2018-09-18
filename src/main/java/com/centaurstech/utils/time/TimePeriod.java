package com.centaurstech.utils.time;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Feliciano on 9/9/2018.
 */
public enum TimePeriod implements ComparablePeriod{

    Early_Morning,Morning,
    Beforenoon,
    Noon,
    Afternoon,
    Dawn,
    Evening,
    Late_Night,
    UNKNOWN;

    public Boolean inPeriod(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return inPeriod(cal);
    }

    public Boolean inPeriod(Calendar cal) {
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        switch (this) {
            case Early_Morning:
                if(hours>=1&&hours<6)
                    return true;
                break;
            case Morning:
                if (hours >= 6 && hours <9)
                    return true;
                break;
            case Beforenoon:
                if (hours >= 8 && hours < 12)
                    return true;
                break;
            case Noon:
                if (hours >= 11 && hours < 14)
                    return true;
                break;
            case Afternoon:
                if (hours >= 14 && hours < 19)
                    return true;
                break;
            case Dawn:
                if(hours>=18&&hours<20)
                    return true;
                break;
            case Evening:
                if (hours >= 19 && hours <=23)
                    return true;
                break;
            case Late_Night:
                if(hours>=22||hours<1)
                    return true;
                break;
            case UNKNOWN:
                return true;
        }
        return false;
    }

    public static TimePeriod fromWord(String chinese) {
        switch (chinese) {
            case "半夜":
            case "凌晨":
                return Early_Morning;
            case "早晨":
            case "早上":
                return Morning;
            case "上午":
                return Beforenoon;
            case "中午":
            case "午时":
                return Noon;
            case "下午":
                return Afternoon;
            case "傍晚":
                return Dawn;
            case "晚上":
            case "夜晚":
                return Evening;
            case "深夜":
                return Late_Night;
            default:
                return UNKNOWN;
        }
    }

}
