package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bigpharma.covtact.database.ContactDatabaseHelper;
import com.bigpharma.covtact.model.ContactModel;
import com.bigpharma.covtact.util.Util;

import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameEditText;
    private EditText noteEditText;
    private Button dateButton;
    private Button addButton;
    private Calendar calendar;

    private void initComponents() {
        dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        AddContactActivity.this,
                        R.style.DialogTheme,
                        AddContactActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });

        dateButton.setText(Util.dateToDisplayString(calendar.getTime()));

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
                        calendar.getTime(),
                        noteEditText.getText().toString());

                ContactDatabaseHelper databaseHelper = new ContactDatabaseHelper(AddContactActivity.this);
                databaseHelper.addContact(contactModel);

                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        calendar = Calendar.getInstance();
        initComponents();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar.set(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH,i1);
        calendar.set(Calendar.DAY_OF_MONTH,i2);
        dateButton.setText(Util.dateToDisplayString(calendar.getTime()));
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