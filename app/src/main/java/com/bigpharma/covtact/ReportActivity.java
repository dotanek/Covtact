package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;
import com.bigpharma.covtact.firebase.FirestoreHelper;
import com.google.firebase.auth.FirebaseAuth;

public class ReportActivity extends AppCompatActivity {

    private Button reportButton;
    private Button checkButton;

    private FirestoreHelper fdb = new FirestoreHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initComponents();
    }


    private void initComponents() {

        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //zwraca listę na listy punktów - lista nie nie jest po kolei czasowo. Na ten moment zwraca wszystkie trasy wrzucone z wyjątkiem trasy użytkownika
                fdb.getVirusPaths();
            }
        });

        reportButton = (Button) findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                PathDatabaseHelper pdb = db.getPathDatabaseHelper();
                fdb.PublishPath(pdb.getPathPointByPath(pdb.getOwnedPathWithMaxId()));
            }
        });


    }



}