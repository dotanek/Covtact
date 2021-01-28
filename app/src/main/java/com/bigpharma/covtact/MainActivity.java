package com.bigpharma.covtact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bigpharma.covtact.firebase.FirestoreHelper;
import com.bigpharma.covtact.model.PathPointModel;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    // Buttons

    private Button contactsButton;
    private Button settingsButton;
    private Button mapButton;
    private Button reportButton;
    private Button checkButton;

    //tmp
    private Button debug_logout;

    //ogniobaza
    private FirestoreHelper fdb = new FirestoreHelper();

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
        contactsButton = (Button) findViewById(R.id.confirmYesButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactsIntent = new Intent(view.getContext(),ContactsActivity.class);
                view.getContext().startActivity(contactsIntent);
            }
        });

        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this,"Fetching data from database.", Toast.LENGTH_LONG).show();

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           List<List<PathPointModel>> pathList = fdb.getThings();
                           /*Toast.makeText(MainActivity.this,"Preparing data for check.", Toast.LENGTH_SHORT).show();*/
                           Log.wtf("test","Preparing data!");
                           for (List<PathPointModel> path : pathList) {
                               Collections.sort(path, new Comparator<PathPointModel>() {
                                   @Override
                                   public int compare(PathPointModel p1, PathPointModel p2) {
                                       return p1.getDateHHMM() - p2.getDateHHMM();
                                   }
                               });
                               Log.wtf("Path","-----------------------------------------");
                               for (PathPointModel point : path) {
                                   Log.wtf("Point",Integer.toString(point.getDateHHMM()));
                               }
                           }
                           /*.makeText(MainActivity.this,"Checking potential exposures.", Toast.LENGTH_SHORT).show();
                           Toast.makeText(MainActivity.this,"There were no exposures detected!", Toast.LENGTH_LONG).show();*/
                       } catch (ExecutionException e) {
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }).start();
           }
        });

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