package com.bigpharma.covtact;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class BackgroundService extends Service {

    private final int NOTIFICATION_ID = 101;
    private final String NOTIFICATION_CHANNEL_ID = "com.bigpharma.covtact.BackgroundService";

    public class BackgroundServiceBinder extends Binder {
        BackgroundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BackgroundService.this;
        }
    }

    private final IBinder binder = new BackgroundServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private LocationManager locationManager;
    private LocationServiceListener locationListener;

    public LocationService getLocationService() {
        return locationListener;
    }

    private void notification() { // create the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle("Covtact")
                .setContentText("background service")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Covtact background service", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // create the pending intent and add to the notification
        Intent intent = new Intent(this, BackgroundService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationBuilder.setContentIntent(pendingIntent);
        // send the notification
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        startForeground(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void initLocationListener() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationServiceListener();
        for (String providerName : locationManager.getAllProviders()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(providerName, 5000, 1, locationListener);
            }
        }
    }

    private void stopLocationListener() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocationListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
