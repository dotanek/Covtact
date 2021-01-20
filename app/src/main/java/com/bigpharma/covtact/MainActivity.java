package com.bigpharma.covtact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.security.Permission;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    // Buttons

    private Button contactsButton;
    private Button settingsButton;
    private Button mapButton;
    private Button reportButton;

    //tmp
    private Button debug_logout;
    //ogniobaza

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }   else {
            initComponents();
            startLocationService();
        }
    }

    private void startLocationService() {
        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            List<String> permissionsToAsk = new LinkedList<String>();
            List<String> grantedPermissions = new LinkedList<String>();
            List<String> deniedPermissions = new LinkedList<String>();
            for(int i = 0; i < permissions.length; i ++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permissions[i]);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if(grantedPermissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Intent intentLocationService = new Intent(this, BackgroundService.class);
                startService(intentLocationService);
            } else if(deniedPermissions.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsToAsk.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(!permissionsToAsk.isEmpty()) {
                ActivityCompat.requestPermissions(this,(String[]) permissionsToAsk.toArray(),REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void initComponents() {
        contactsButton = (Button) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactsIntent = new Intent(view.getContext(),ContactsActivity.class);
                view.getContext().startActivity(contactsIntent);
            }
        });

        /*settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(view.getContext(),SettingsActivity.class);
                view.getContext().startActivity(settingsIntent);
            }
        });*/

        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(view.getContext(),MapActivity.class);
                view.getContext().startActivity(mapIntent);
            }
        });

        reportButton = (Button) findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportIntent = new Intent(view.getContext(),ReportActivity.class);
                view.getContext().startActivity(reportIntent);
            }
        });



        debug_logout = (Button) findViewById(R.id.debug_logout);
        debug_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(view.getContext(), LoginActivity.class);
                view.getContext().startActivity(loginIntent);
            }
        });

    }
}