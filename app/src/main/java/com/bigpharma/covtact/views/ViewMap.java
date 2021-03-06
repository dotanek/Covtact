package com.bigpharma.covtact.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.bigpharma.covtact.BuildConfig;
import com.bigpharma.covtact.R;
import com.bigpharma.covtact.util.Util;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class ViewMap extends MapView implements LocationListener {

    private boolean needCenter = true;
    private IMapController mapController;
    private LocationOverlay locationOverlay;
    private RouteOverlay routeOverlay;
    private Context applicationContext;
    private Location lastLocation;

    public ViewMap(Context context) {
        super(context);
        applicationContext = context;
        init();
    }
    public ViewMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        applicationContext = context;
        init();
    }
    private void init() {
        org.osmdroid.config.Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setTileSource(TileSourceFactory.MAPNIK);
        //locationOverlay = new MyLocationNewOverlay(this);
        //Bitmap normalIcon = BitmapFactory.decodeResource(applicationContext.getResources(),R.drawable.ex_person);
        //Bitmap moveIcon = BitmapFactory.decodeResource(applicationContext.getResources(), R.drawable.ex_person_nav);
        //locationOverlay.setPersonIcon(normalIcon);
        //locationOverlay.setDirectionArrow(normalIcon,moveIcon);
        //TODO: Set arrow icon
        //this.getOverlays().add(locationOverlay);

        routeOverlay = new RouteOverlay(this);
        routeOverlay.setTheme(RouteOverlay.Theme.Red);
        this.getOverlays().add(routeOverlay);

        mapController = new MapController(this);
        mapController.setZoom(11);

        locationOverlay = new LocationOverlay(this);
        this.getOverlays().add(locationOverlay);
    }

    public void centerMap() {
        if(lastLocation != null) {
            mapController.setCenter(Util.GeoPointFromLocation(lastLocation));
        } else {
            needCenter = true;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.lastLocation = location;
        if(needCenter) {
            mapController.setCenter(Util.GeoPointFromLocation(location));
            needCenter = false;
        }
        if(locationOverlay != null) {
            locationOverlay.setPosition(Util.GeoPointFromLocation(location));
        }
        if(routeOverlay != null) {
            routeOverlay.addPoint(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(@NonNull String provider) { }

    @Override
    public void onProviderDisabled(@NonNull String provider) { }
}
