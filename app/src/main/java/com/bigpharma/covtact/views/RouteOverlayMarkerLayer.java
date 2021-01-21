package com.bigpharma.covtact.views;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlayMarkerLayer extends FolderOverlay {
    List<RouteOverlayMarker> markerList;
    List<GeoPoint> points;
    MapView mapView;
    private RouteOverlay.Theme theme;

    public RouteOverlayMarkerLayer(MapView mapView) {
        super();
        this.mapView = mapView;
        this.markerList = new ArrayList<>();
        this.points = new ArrayList<>();
        this.theme = RouteOverlay.Theme.Blue;
    }

    public void setTheme(RouteOverlay.Theme theme) {
        this.theme = theme;
        for (int i = 0; i < this.markerList.size(); i++) {
            this.markerList.get(i).setTheme(this.theme);
        }
    }

    public void setPoints(List<GeoPoint> points) {
        this.points = points;
        for (int i = 0; i < this.markerList.size(); i++) {
            this.remove(markerList.get(i));
        }
        this.markerList.clear();
        for (int i = 0; i < this.points.size(); i++) {
            RouteOverlayMarker a = new RouteOverlayMarker(mapView);
            a.setTheme(this.theme);
            a.setPosition(this.points.get(i));
            this.markerList.add(a);
            this.add(a);
        }
    }


}
