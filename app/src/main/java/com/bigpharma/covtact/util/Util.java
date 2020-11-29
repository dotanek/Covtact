package com.bigpharma.covtact.util;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static GeoPoint GeoPointFromLocation(Location location) {
        if(location.hasAltitude()) {
            return new GeoPoint(location.getLatitude(),location.getLongitude(),location.getAltitude());
        } else {
            return new GeoPoint(location.getLatitude(),location.getLongitude());
        }
    }
    public static String dateToSqliteString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        return dateFormat.format(date);
    }
    public static Date sqliteStringToDate(String sqliteStringDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        return dateFormat.parse(sqliteStringDate);
    }
}
