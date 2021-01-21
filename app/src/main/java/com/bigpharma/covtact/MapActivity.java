package com.bigpharma.covtact;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bigpharma.covtact.views.ViewMap;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 2;
    private ServiceConnection backgroundServiceConnection;
    private BackgroundService backgroundService;
    private ViewMap mapView;

    private void connectBackgroundService() {
        Intent intent = new Intent(this,BackgroundService.class);
        backgroundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                backgroundService = ((BackgroundService.BackgroundServiceBinder)service).getService();
                backgroundService.getLocationService().addListener(mapView);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                backgroundService.getLocationService().removeListener(mapView);
                backgroundService = null;
            }
        };
        bindService(intent, backgroundServiceConnection, BIND_AUTO_CREATE);
    }
    private void disconnectBackgroundService() {
        unbindService(backgroundServiceConnection);
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        requestPermissionsIfNecessary(new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        });;
        mapView = (ViewMap) findViewById(R.id.mapView);
        connectBackgroundService();

        Button centerBtn = (Button) findViewById(R.id.centerBtn);
        centerBtn.setOnClickListener((x) -> {
            this.mapView.centerMap();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectBackgroundService();
    }
}