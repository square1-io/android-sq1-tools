package io.square1.tools.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by roberto on 13/07/2016.
 */
public class DateUtils {

    //"yyyy-MM-dd HH:mm:ss
    public static final String FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

    public static Date parseStringDate(String date)  {
        return parseStringDate(date, FORMAT_1);
    }

    public static Date parseStringDate(String date, final String format)  {
        return parseStringDate(date, format, TimeZone.getDefault());
    }

    public static Date parseStringDate(String date, final String format, TimeZone tmz)  {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(tmz);
            return sdf.parse(date);
        }catch (Exception e){}

        return null;
    }

    public static String dateToString(Date date, final String format){
        return dateToString(date, format, TimeZone.getDefault());
    }

    public static String dateToString(Date date, final String format, TimeZone tmz){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(tmz);
        return sdf.format(date);
    }
}
