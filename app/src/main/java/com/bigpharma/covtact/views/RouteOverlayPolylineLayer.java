package com.bigpharma.covtact.views;

import android.graphics.Color;
import android.util.Log;

import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlayPolylineLayer extends Polyline {
    private RouteOverlay.Theme theme;

    public RouteOverlayPolylineLayer(MapView mapView) {
        super(mapView);
        this.setPoints(new ArrayList<GeoPoint>());
        this.setInfoWindow(null);
        this.setTheme(RouteOverlay.Theme.Blue);
    }

    @Override
    public void setPoints(List<GeoPoint> points) {
        super.setPoints(points);
    }
    void setTheme(RouteOverlay.Theme theme) {
        int colorRed = 0xFF730E0E;
        int colorBlue = 0xFF0E2F73;
        this.theme = theme;
        if(this.getFillPaint() == null) {
            try {
                if(this.theme == RouteOverlay.Theme.Red) {
                    this.setColor(colorRed);
                } else {
                    this.setColor(colorBlue);
                }
            } catch (Exception e) {

            }
            return;
        }
        if(this.theme == RouteOverlay.Theme.Red) {
            this.getFillPaint().setColor(colorRed);
        } else {
            this.getFillPaint().setColor(colorBlue);
        }
    }
}

