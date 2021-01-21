package com.bigpharma.covtact;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;
import com.bigpharma.covtact.util.Util;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

interface LocationService {
    void addListener(LocationListener ll);
    void removeListener(LocationListener ll);
    void resetPath();
}

public class LocationServiceListener implements LocationListener, LocationService {
    private final ReentrantLock registerPathPointLock = new ReentrantLock();
    private final Context applicationContext;
    private final List<LocationListener> listenerList;
    private Location lastLocation = null;
    private PathModel lastestOwnedPath = null;
    private PathPointModel lastestOwnedPathPoint = null;
    private PathDatabaseHelper pathDatabaseHelper = null;
    private int pathPointCounter = 0;
    private int lastDateHHMM = -1;
    private boolean disableRegistering = false;

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
        DatabaseHelper databaseHelper = new DatabaseHelper(applicationContext);
        pathDatabaseHelper = databaseHelper.getPathDatabaseHelper();
        createPathOwnedIfNotExist();
    }

    private void createPathOwnedIfNotExist() {
        registerPathPointLock.lock();
        lastestOwnedPath = pathDatabaseHelper.getOwnedPathWithMaxId();
        Log.i("createPathOwnedIfNotExist",String.valueOf(lastestOwnedPath));
        if(lastestOwnedPath == null) {
            Date startDate = new Date();
            PathModel newOwnedPath = new PathModel(startDate);
            newOwnedPath.setEndDate(startDate);
            newOwnedPath.setDeviceOwner(true);
            lastestOwnedPath = pathDatabaseHelper.addPath(newOwnedPath);
            pathPointCounter = 0;
        }

        registerPathPointLock.unlock();
    }

    private void registerPathPoint(Location location) {
        Date date = new Date();
        int dateHHMM = Util.dateToHHMMInteger(date);
        if(dateHHMM % 5 != 0 || dateHHMM == lastDateHHMM) {
            return;
        }
        lastDateHHMM = dateHHMM;
        PathPointModel pathPointModel = new PathPointModel(date,location.getLongitude(),location.getLatitude());

        if(lastestOwnedPath != null) {
            String tag = String.format("%d %s",
                    Thread.currentThread().getId(),
                    Thread.currentThread().getName());

            registerPathPointLock.lock();

            if(lastestOwnedPathPoint == null) {
                lastestOwnedPathPoint = pathDatabaseHelper.getPathPointInPathWithMaxId(lastestOwnedPath);
            }
            pathDatabaseHelper.addPathPointToPath(lastestOwnedPath,pathPointModel);
            lastestOwnedPath = pathDatabaseHelper.getOwnedPathWithMaxId();
            lastestOwnedPathPoint = pathDatabaseHelper.getPathPointInPathWithMaxId(lastestOwnedPath);

            pathPointCounter += 1;
            if(pathPointCounter > 100) {
                Date startDate = new Date();
                PathModel newOwnedPath = new PathModel(startDate);
                newOwnedPath.setEndDate(startDate);
                newOwnedPath.setDeviceOwner(true);
                lastestOwnedPath = pathDatabaseHelper.addPath(newOwnedPath);
                pathPointCounter = 0;
            }

            registerPathPointLock.unlock();

        } else {
            Log.i("registerPathPoint","lastestOwnedPath = null");
            createPathOwnedIfNotExist();
        }
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

    public void resetPath() {
        disableRegistering = true;
        registerPathPointLock.lock();
        pathDatabaseHelper.resetAllPaths();
        lastestOwnedPath = null;
        lastestOwnedPathPoint = null;
        pathPointCounter = 0;
        lastDateHHMM = -1;
        createPathOwnedIfNotExist();
        registerPathPointLock.unlock();
        disableRegistering = false;
    }

    @Override
    public void onLocationChanged(@NonNull final Location location) {
        lastLocation = location;
        for(LocationListener ll: listenerList) ll.onLocationChanged(location);
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                if(!disableRegistering) {
                    registerPathPoint(location);
                }
            }
        });
        t.start();
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
