package ru.startandroid.weather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static boolean isToday(Date d1) {
        return isTheSameDay(d1, new Date());
    }

    public static boolean isTheSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        Calendar c1 = toCalendar(d1);
        Calendar c2 = toCalendar(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DATE) == c2.get(Calendar.DATE);
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String getDateTimeString(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
//        return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date);
    }
}