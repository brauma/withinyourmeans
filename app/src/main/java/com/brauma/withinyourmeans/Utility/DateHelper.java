package com.brauma.withinyourmeans.Utility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static long strDateToEpoch(String strDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(strDate);
            long epoch = date.getTime();
            return epoch;
        } catch(ParseException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static long dateToEpoch(Date date){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(date);
        return strDateToEpoch(formatted);
    }

    public static String epochToStrDate(long epoch){
        Date date = new Date(epoch);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return  format.format(date);
    }

    public static TimeIntervall getMonth(Date now){
        long from, to;

        Calendar c = Calendar.getInstance();
        c.setTime(now);

        c.set(Calendar.DAY_OF_MONTH, 1);
        from = dateToEpoch(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        to = dateToEpoch(c.getTime());

        return new TimeIntervall(from, to);
    }

    public static Date getPreviousMonth(Calendar c){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        Calendar result = c;

        if(month == 1){
            result.set(Calendar.MONTH, 12);
            result.set(Calendar.YEAR, year - 1);
        } else {
            result.set(Calendar.MONTH, month - 1);
        }

        return result.getTime();
    }
}
