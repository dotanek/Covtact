package com.bigpharma.covtact.views;


import android.util.Log;

import com.bigpharma.covtact.DatabaseHelper;
import com.bigpharma.covtact.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;
import com.bigpharma.covtact.util.Util;

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
        PathModel pathLastest  = pathDatabaseHelper.getOwnedPathWithMaxId();
        ArrayList<PathModel> paths = new ArrayList<PathModel>();
        paths.add(pathLastest);
        PathModel tempPath = pathDatabaseHelper.getPathById(Util.getLastElement(paths).getId()-1,true);
        while (tempPath != null) {
            paths.add(tempPath);
            tempPath = pathDatabaseHelper.getPathById(Util.getLastElement(paths).getId()-1,true);
        }

        for(int pIndex = paths.size()-1; pIndex>=0; pIndex--) {
            List<PathPointModel> pathPoints = pathDatabaseHelper.getPathPointByPath(paths.get(pIndex));
            for(int index = 0;index < pathPoints.size(); index++) {
                points.add(pathPoints.get(index).toGeoPoint());
            }
        }
        this.setPoints(points);
    }
}