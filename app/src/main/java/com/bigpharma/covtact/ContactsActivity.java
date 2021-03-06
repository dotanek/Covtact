package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigpharma.covtact.database.ContactDatabaseHelper;
import com.bigpharma.covtact.model.ContactModel;
import com.bigpharma.covtact.util.Util;

import java.util.Calendar;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private LinearLayout contactsContainer;
    private ScrollView contactsScrollView;
    private Button addContactButton;
    Dialog detailsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initComponents();

        removeObsoleteContacts();

        renderContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderContacts();
    }

    private void removeObsoleteContacts() {
        ContactDatabaseHelper databaseHelper = new ContactDatabaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -14);
        databaseHelper.removeContactsBeforeDate(calendar.getTime());
    }

    private void renderContacts() {
        contactsContainer.removeAllViews();
        ContactDatabaseHelper databaseHelper = new ContactDatabaseHelper(this);
        try {
            List<ContactModel> contactModelList = databaseHelper.getContacts();
            for (ContactModel contactModel : contactModelList) {
                Button entry = initEntry(contactModel);
                contactsContainer.addView(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        contactsContainer = (LinearLayout) findViewById(R.id.contactsContainer);

        contactsScrollView = (ScrollView) findViewById(R.id.contactsScrollView);
        contactsScrollView.post(new Runnable() {
            @Override
            public void run() {
                contactsScrollView.fullScroll(View.FOCUS_DOWN); // Scrolls down all the way to the bottom of the list.
            }
        });

        addContactButton = (Button) findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(view.getContext(),AddContactActivity.class);
                view.getContext().startActivity(addContactIntent);
            }
        });

        detailsDialog = new Dialog(this);
    }

    Button initEntry(final ContactModel contactModel) {
        Button entry = new Button(getApplicationContext());
        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsName.leftMargin = 150;
        paramsName.rightMargin = 150;
        entry.setLayoutParams(paramsName);
        entry.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorGreenLight)));

        entry.setTextColor(Color.WHITE);
        entry.setTextSize(20);
        entry.setTypeface(Typeface.createFromAsset(getAssets(),
                "font/harabara.ttf"));
        entry.setText(contactModel.getName());
        entry.setId(contactModel.getId());

        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, contactModel);
            }
        });
        return entry;
    }

    public void showPopup(View v, final ContactModel contactModel) {
        detailsDialog.setContentView(R.layout.contact_popup);
        detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText nameEditText = detailsDialog.findViewById(R.id.nameEditText);
        nameEditText.setText(contactModel.getName());

        final EditText noteEditText = detailsDialog.findViewById(R.id.noteEditText);
        noteEditText.setText(contactModel.getNote());

        final Button saveButton = detailsDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contactName = nameEditText.getText().toString();

                if (contactName.length() == 0) {
                    nameEditText.requestFocus();
                    return;
                }

                contactModel.setName(contactName);
                contactModel.setNote(noteEditText.getText().toString());

                ContactDatabaseHelper databaseHelper = new ContactDatabaseHelper(ContactsActivity.this);
                databaseHelper.updateContact(contactModel);

                detailsDialog.cancel();
                ContactsActivity.this.onResume();
            }
        });

        nameEditText.setOnTouchListener(new View.OnTouchListener() { // Doesn't fix selection in the middle of string. TODO make the cursor go to the end of string on touch.
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nameEditText.onTouchEvent(motionEvent);
                nameEditText.setSelection(nameEditText.getText().length());
                return false;
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight)));
                saveButton.setTextColor(Color.WHITE);
            }
        });

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(true);
                saveButton.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorBlueLight)));
                saveButton.setTextColor(Color.WHITE);
            }
        });

        TextView dateTextView = detailsDialog.findViewById(R.id.dateTextView);
        dateTextView.setText(Util.dateToDisplayString(contactModel.getDate()));

        Button removeButton = (Button) detailsDialog.findViewById(R.id.removeButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactDatabaseHelper databaseHelper = new ContactDatabaseHelper(ContactsActivity.this);
                databaseHelper.removeContact(contactModel.getId());
                detailsDialog.cancel();
                ContactsActivity.this.onResume();
            }
        });
        detailsDialog.show();
    }
}