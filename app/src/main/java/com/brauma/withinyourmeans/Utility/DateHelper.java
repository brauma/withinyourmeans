package com.brauma.withinyourmeans.Utility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static long strDateToEpoch(String strDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(strDate);
            long epoch = date.getTime();
            return epoch;
        } catch(ParseException e){
            Log.e("PARSE ERROR", strDate);
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
        String formatted = format.format(date);
        return formatted;
    }

    public static Date epochToDate(long epoch){
        Date date = new Date(epoch);
        return date;
    }
}
