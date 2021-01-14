package com.bigpharma.covtact.views;


import android.util.Log;

import com.bigpharma.covtact.DatabaseHelper;
import com.bigpharma.covtact.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import java.util.*;
public class RouteOverlay extends Polyline {
    private DatabaseHelper databaseHelper = null;
    private PathDatabaseHelper pathDatabaseHelper = null;
    public RouteOverlay(MapView mapView) {
        super(mapView);
        databaseHelper = new DatabaseHelper(mapView.getContext());
        pathDatabaseHelper = databaseHelper.getPathDatabaseHelper();
        List<GeoPoint> points = new ArrayList<>();
        PathModel path  = pathDatabaseHelper.getOwnedPathWithMaxId();
        List<PathPointModel> pathPoints = pathDatabaseHelper.getPathPointByPath(path);
        for(int index = 0;index < pathPoints.size(); index++) {
            points.add(pathPoints.get(index).toGeoPoint());
            Log.i("xd",points.toString());
        }
        this.setPoints(points);
    }
}