package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private LinearLayout contactsContainer;
    private ScrollView contactsScrollView;
    private Button addContactButton;

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
                LinearLayout entry = initEntry();
                TextView name = initName();
                Button remove = initRemove();
                name.setText(contactModel.getName());
                entry.addView(name);
                entry.addView(remove);
                entry.setId(contactModel.getId());
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
    }

    LinearLayout initEntry() {
        LinearLayout entry = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams paramsEntry = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsEntry.bottomMargin = 20;
        entry.setLayoutParams(paramsEntry);
        entry.setGravity(Gravity.CENTER);
        entry.setWeightSum(10);
        entry.setOrientation(LinearLayout.HORIZONTAL);
        return entry;
    }

    TextView initName() {
        TextView name = new TextView(getApplicationContext());
        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(0, 100, 4);
        name.setLayoutParams(paramsName);
        name.setTypeface(Typeface.createFromAsset(getAssets(),
                "font/harabara.ttf"));
        name.setGravity(Gravity.CENTER);
        name.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreenLight));
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(20);
        return name;
    }

    Button initRemove() {
        Button remove =  new Button(getApplicationContext());
        LinearLayout.LayoutParams paramsRemove = new LinearLayout.LayoutParams(
                0, 120, 2.5f);
        paramsRemove.leftMargin = 10;
        remove.setLayoutParams(paramsRemove);
        remove.setTypeface(Typeface.createFromAsset(getAssets(),
                "font/harabara.ttf"));
        remove.setGravity(Gravity.CENTER);
        remove.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorRedLight)));
        remove.setTextColor(Color.parseColor("#FFFFFF"));
        remove.setTextSize(15);
        remove.setText("Remove");
        return remove;
    }
}