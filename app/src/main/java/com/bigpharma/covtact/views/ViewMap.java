package com.bigpharma.covtact.views;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.BuildConfig;
import com.bigpharma.covtact.util.Util;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ViewMap extends MapView implements LocationListener {

    private boolean initialCentered = false;
    private IMapController mapController;
    public ViewMap(Context context) {
        super(context);
        init(context);
    }
    public ViewMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setTileSource(TileSourceFactory.MAPNIK);
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),this);
        locationOverlay.enableMyLocation();
        getOverlays().add(locationOverlay);
        mapController = new MapController(this);
        mapController.setZoom(10);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(!initialCentered) {
            mapController.setCenter(Util.GeoPointFromLocation(location));
            initialCentered = true;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
