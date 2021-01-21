package com.bigpharma.covtact.views;


import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;
import com.bigpharma.covtact.util.Util;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.*;
public class RouteOverlay extends FolderOverlay {
    int lastHHMM = -1;
    public void addPoint(@NonNull Location location) {
        Date x = new Date();
        int time = Util.dateToHHMMInteger(x);
        if(time != lastHHMM && time % 5 == 0 && points != null) {
            lastHHMM = time;
            points.add(Util.GeoPointFromLocation(location));
            this.setPoints(this.points);
        }
    }

    public enum  Theme {
        Blue,
        Red
    }
    private RouteOverlayPolylineLayer polylineLayer;
    private RouteOverlayMarkerLayer markerLayer;
    private MapView mapView;
    private DatabaseHelper databaseHelper;
    private PathDatabaseHelper pathDatabaseHelper;
    private List<GeoPoint> points = null;
    public RouteOverlay(MapView mapView) {
        super();
        this.mapView = mapView;
        databaseHelper = new DatabaseHelper(mapView.getContext());
        pathDatabaseHelper = databaseHelper.getPathDatabaseHelper();
        this.points = new ArrayList<>();
        PathModel path  = pathDatabaseHelper.getOwnedPathWithMaxId();
        List<PathPointModel> pathPoints = pathDatabaseHelper.getPathPointByPath(path);
        for(int index = 0;index < pathPoints.size(); index++) {
            points.add(pathPoints.get(index).toGeoPoint());
            Log.i("xd",points.toString());
        }
        this.markerLayer = new RouteOverlayMarkerLayer(mapView);
        this.polylineLayer = new RouteOverlayPolylineLayer(mapView);
        this.add(this.polylineLayer);
        this.add(this.markerLayer);
        this.setPoints(points);
        this.setTheme(Theme.Blue);
    }
    void setTheme(Theme theme) {
        this.markerLayer.setTheme(theme);
        this.polylineLayer.setTheme(theme);
        mapView.postInvalidate();
    }
    public void setPoints(List<GeoPoint> points) {
        if(this.polylineLayer != null && this.markerLayer != null) {
            this.polylineLayer.setPoints(this.points);
            this.markerLayer.setPoints(this.points);
            mapView.postInvalidate();
        }
    }
}