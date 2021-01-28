package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigpharma.covtact.database.DatabaseHelper;
import com.bigpharma.covtact.database.PathDatabaseHelper;
import com.bigpharma.covtact.firebase.FirestoreHelper;
import com.bigpharma.covtact.model.ContactModel;
import com.google.firebase.auth.FirebaseAuth;

public class ReportActivity extends AppCompatActivity {

    private Button reportButton;
    private Button checkButton;
    private Button confirmYesButton;
    private Button confirmNoButton;
    private Button infoButton;
    Dialog infoDialog;

    private FirestoreHelper fdb = new FirestoreHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initComponents();
    }

    public void showPopup(View v) {
        infoDialog.setContentView(R.layout.report_info_popup);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.show();
    }

    private void initComponents() {

        confirmYesButton = (Button) findViewById(R.id.confirmYesButton);
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                PathDatabaseHelper pdb = db.getPathDatabaseHelper();
                fdb.PublishPath(pdb.getPathPointByPath(pdb.getOwnedPathWithMaxId()));
                finish();
                Toast.makeText(ReportActivity.this,"Your locations were shared, thank you!", Toast.LENGTH_LONG).show();
            }
        });

        confirmNoButton = (Button) findViewById(R.id.confirmNoButton);
        confirmYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        infoButton = (Button) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        infoDialog = new Dialog(this);

        /*checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //zwraca listę na listy punktów - lista nie nie jest po kolei czasowo. Na ten moment zwraca wszystkie trasy wrzucone z wyjątkiem trasy użytkownika
                fdb.getVirusPaths();
            }
        });*/

        /*reportButton = (Button) findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                PathDatabaseHelper pdb = db.getPathDatabaseHelper();
                fdb.PublishPath(pdb.getPathPointByPath(pdb.getOwnedPathWithMaxId()));
                Toast.makeText(ReportActivity.this,"Your paths were added to the database", Toast.LENGTH_LONG).show();
            }
        });*/
    }
}