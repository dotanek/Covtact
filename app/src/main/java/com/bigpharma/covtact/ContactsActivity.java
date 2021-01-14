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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
        renderContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderContacts();
    }

    private void renderContacts() {
        contactsContainer.removeAllViews();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
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

        TextView nameTextView = (TextView) detailsDialog.findViewById(R.id.nameTextView);
        nameTextView.setText(contactModel.getName());
        TextView dateTextView = (TextView) detailsDialog.findViewById(R.id.dateTextView);
        dateTextView.setText(contactModel.getDate().toString());

        Button removeButton = (Button) detailsDialog.findViewById(R.id.removeButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(ContactsActivity.this);
                databaseHelper.removeContact(contactModel.getId());
                detailsDialog.cancel();
                ContactsActivity.this.onResume();
            }
        });
        detailsDialog.show();
    }
}