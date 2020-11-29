package com.bigpharma.covtact;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.model.PathPointModel;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

interface LocationService {
    void addListener(LocationListener ll);
    void removeListener(LocationListener ll);
}

public class LocationServiceListener implements LocationListener, LocationService {
    private Location lastLocation;
    private List<LocationListener> listenerList;

    public LocationServiceListener() {
        listenerList = new ArrayList<LocationListener>();
        lastLocation = null;
    }

    public LocationServiceListener(Location lastLocation) {
        listenerList = new ArrayList<LocationListener>();
        this.lastLocation = lastLocation;
    }

    public void addListener(LocationListener ll) {
        if(listenerList.contains(ll))
            return;
        if(lastLocation != null) {
            ll.onLocationChanged(lastLocation);
        };
        listenerList.add(ll);
    }

    public void removeListener(LocationListener ll) {
        if(listenerList.contains(ll))
            listenerList.remove(ll);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation = location;
        for(LocationListener ll: listenerList) ll.onLocationChanged(location);
        Date date = DateTimeUtils.toSqlDate(ZonedDateTime.now(ZoneOffset.UTC).toLocalDate());
        PathPointModel pathPointModel = new PathPointModel(date,location.getLongitude(),location.getLatitude());
        Log.i("onLocationChanged",pathPointModel.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        for(LocationListener ll: listenerList) ll.onStatusChanged(provider,status,extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        for(LocationListener ll: listenerList) ll.onProviderDisabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        for(LocationListener ll: listenerList) ll.onProviderDisabled(provider);
    }
}