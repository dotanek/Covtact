package com.bigpharma.covtact.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bigpharma.covtact.R;
import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class RouteOverlayMarker extends Marker {
    private MapView mapView;
    private DatabaseHelper databaseHelper = null;
    private PathDatabaseHelper pathDatabaseHelper = null;

    public RouteOverlayMarker(MapView mapView) {
        super(mapView);
        this.mapView = mapView;
        this.setTheme(RouteOverlay.Theme.Blue);
        this.setInfoWindow(null);
    }

    @Override
    public void setPosition(GeoPoint position) {
        super.setPosition(position);
    }
    public void setTheme(RouteOverlay.Theme theme) {
        if(theme == RouteOverlay.Theme.Red) {
            Bitmap pathPointIconBitmap = BitmapFactory.decodeResource(mapView.getContext().getResources(),R.drawable.user_point);
            BitmapDrawable pathPointIcon = new BitmapDrawable(mapView.getContext().getResources(), pathPointIconBitmap);
            this.setIcon(pathPointIcon);
            this.setAnchor(0.5f,0.5f);
        } else {
            Bitmap pathPointIconBitmap = BitmapFactory.decodeResource(mapView.getContext().getResources(),R.drawable.path_point);
            BitmapDrawable pathPointIcon = new BitmapDrawable(mapView.getContext().getResources(), pathPointIconBitmap);
            this.setIcon(pathPointIcon);
            this.setAnchor(0.5f,0.5f);
        }
    }
}
