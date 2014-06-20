package com.example.instantscore.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Utils {
    private static String patternDateTime = "MMM dd HH:mm";
    private static String patternDate = "MMM dd";
    private static String patternTime = "HH:mm";


    public static Calendar buildDateTime(String date, String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patternDateTime);
        Calendar asd = Calendar.getInstance();
        asd.setTime(sdf.parse(date+" "+time));
        return asd;
    }

    public static Calendar buildTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patternTime);
        Calendar asd = Calendar.getInstance();
        asd.setTime(sdf.parse(time));
        return asd;
    }

    public static Calendar convertDateTime(Calendar cal){
        TimeZone T2 = TimeZone.getDefault();
        int diff = T2.getOffset(Calendar.getInstance().getTimeInMillis())/1000/3600;
        cal.add(Calendar.HOUR_OF_DAY, diff);
        return cal;
    }

    public static String getTimeFrom(Calendar cal){
        return format(cal, patternTime);
    }

    public static String getDateFrom(Calendar cal){
        return format(cal, patternDate);
    }

    private static String format(Calendar cal, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(cal.getTime());
    }

}
