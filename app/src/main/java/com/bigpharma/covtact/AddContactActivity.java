package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bigpharma.covtact.DataType.ContactDate;

import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameEditText;
    private EditText noteEditText;
    private Button dateButton;
    private Button addButton;
    private ContactDate date;

    private void initComponents() {
        dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setText(date.toString());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AddContactActivity.this,
                        R.style.DialogTheme,
                        AddContactActivity.this,
                        date.year,
                        date.month,
                        date.day
                );
                dialog.show();
            }
        });

        nameEditText = findViewById(R.id.nameEditText);
        noteEditText = findViewById(R.id.noteEditText);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contactName = nameEditText.getText().toString();

                if (contactName.length() == 0) {
                    nameEditText.requestFocus();
                    return;
                }

                ContactModel contactModel = new ContactModel(contactName,
                        date,
                        noteEditText.getText().toString());

                DatabaseHelper databaseHelper = new DatabaseHelper(AddContactActivity.this);
                databaseHelper.addContact(contactModel);

                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        date = new ContactDate();

        initComponents();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date.year = i;
        date.month = i1;
        date.day = i2;
        dateButton.setText(date.toString());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { // Removes focus from EditText when touching outside of it.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}