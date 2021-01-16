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

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

interface LocationService {
    void addListener(LocationListener ll);
    void removeListener(LocationListener ll);
}

public class LocationServiceListener implements LocationListener, LocationService {
    private final ReentrantLock registerPathPointLock = new ReentrantLock();
    private PathModel lastestOwnedPath = null;
    private PathPointModel lastestOwnedPathPoint = null;
    private DatabaseHelper databaseHelper = null;
    private PathDatabaseHelper pathDatabaseHelper = null;
    private int pathPointCounter = 0;
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
        databaseHelper = new DatabaseHelper(applicationContext);
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
        PathPointModel pathPointModel = new PathPointModel(date,location.getLongitude(),location.getLatitude());

        if(lastestOwnedPath != null) {
            String tag = String.format("%d %s",
                    Thread.currentThread().getId(),
                    Thread.currentThread().getName());

            registerPathPointLock.lock();

            if(lastestOwnedPathPoint == null) {
                lastestOwnedPathPoint = pathDatabaseHelper.getPathPointInPathWithMaxId(lastestOwnedPath);
            }
            Pair<PathModel,PathPointModel> pair = new Pair<PathModel,PathPointModel>(null,null);
            if(lastestOwnedPathPoint == null) {
                pair = pathDatabaseHelper.addPathPointToPath(lastestOwnedPath,pathPointModel);
            }
            else if(lastestOwnedPathPoint.distanceTo(pathPointModel) > 0) {
                pair = pathDatabaseHelper.addPathPointToPath(lastestOwnedPath,pathPointModel);
            }
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

    @Override
    public void onLocationChanged(@NonNull final Location location) {
        lastLocation = location;
        for(LocationListener ll: listenerList) ll.onLocationChanged(location);
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                registerPathPoint(location);
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
