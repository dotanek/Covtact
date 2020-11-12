package com.bigpharma.covtact.util;

import android.location.Location;

import org.osmdroid.util.GeoPoint;

public class Util {
    public static GeoPoint GeoPointFromLocation(Location location) {
        if(location.hasAltitude()) {
            return new GeoPoint(location.getLatitude(),location.getLongitude(),location.getAltitude());
        } else {
            return new GeoPoint(location.getLatitude(),location.getLongitude());
        }
    }
}
