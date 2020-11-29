package com.bigpharma.covtact;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.model.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

interface LocationService {
    void addListener(LocationListener ll);
    void removeListener(LocationListener ll);
}

public class LocationServiceListener implements LocationListener, LocationService {
    private PathModel lastestOwnedPath = null;
    private PathDatabaseHelper pathDatabaseHelper = null;
    private Context applicationContext;
    private Location lastLocation;
    private final List<LocationListener> listenerList;

    public LocationServiceListener(Context context) {
        listenerList = new ArrayList<LocationListener>();
        lastLocation = null;
        this.applicationContext = context;
        this.initialize();
    }

    public LocationServiceListener(Context context,Location lastLocation) {
        listenerList = new ArrayList<LocationListener>();
        this.lastLocation = lastLocation;
        this.applicationContext = context;
        this.initialize();
    }

    private void initialize() {
        pathDatabaseHelper = new PathDatabaseHelper(applicationContext);
        lastestOwnedPath = pathDatabaseHelper.getLastestOwnedPath();
        if(lastestOwnedPath == null) {
            Date startDate = new Date();
            PathModel newOwnedPath = new PathModel(startDate);
            newOwnedPath.setEndDate(startDate);
            newOwnedPath.setDeviceOwner(true);
            lastestOwnedPath = pathDatabaseHelper.addPath(newOwnedPath);
        }
        Log.i("LocationService",lastestOwnedPath.toString());
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
        Date date = new Date();
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
