package com.bigpharma.covtact.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.bigpharma.covtact.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class LocationOverlay extends Marker {
    private MapView mapView;

    public LocationOverlay(MapView mapView) {
        super(mapView);
        this.mapView = mapView;
        Bitmap pathPointIconBitmap = BitmapFactory.decodeResource(mapView.getContext().getResources(), R.drawable.ex_person_nav);
        BitmapDrawable pathPointIcon = new BitmapDrawable(mapView.getContext().getResources(), pathPointIconBitmap);
        this.setIcon(pathPointIcon);
        this.setAnchor(0.5f,0.5f);
        this.setInfoWindow(null);
    }

    @Override
    public void setPosition(GeoPoint position) {
        super.setPosition(position);
        mapView.postInvalidate();
    }
}
