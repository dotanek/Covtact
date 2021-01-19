package com.bigpharma.covtact.util;

import android.location.Location;

import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;

import org.osmdroid.util.GeoPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Util {
    private Util() {}

    public static PathModel getLastElement(List<PathModel> list) {
        int lastIndex = list.size()-1;
        return  list.get(lastIndex);
    }

    public static GeoPoint GeoPointFromLocation(Location location) {
        if(location.hasAltitude()) {
            return new GeoPoint(location.getLatitude(),location.getLongitude(),location.getAltitude());
        } else {
            return new GeoPoint(location.getLatitude(),location.getLongitude());
        }
    }
    public static Integer dateToHHMMInteger(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.ENGLISH);
        return Integer.parseInt(dateFormat.format(date));
    }
    public static String dateToSqliteString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        return dateFormat.format(date);
    }
    public static Date sqliteStringToDate(String sqliteStringDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            return dateFormat.parse(sqliteStringDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToDisplayString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        String dateStr = ((day > 9) ? Integer.toString(day) : "0" + day);
        dateStr += "/" + ((month+1 > 9) ? month+1 : "0" + (month+1));
        dateStr += "/" + year;
        return dateStr;
    }
}
