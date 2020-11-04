package com.bigpharma.covtact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ContactsActivity extends AppCompatActivity {

    private LinearLayout contactsContainer;
    private ScrollView contactsScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsScrollView = (ScrollView) findViewById(R.id.contactsScrollView);
        contactsContainer = (LinearLayout) findViewById(R.id.contactsContainer);

        contactsScrollView.post(new Runnable() {
            @Override
            public void run() {
                contactsScrollView.fullScroll(View.FOCUS_DOWN); // Scrolls down all the way to the bottom of the list.
            }
        });

        for (int i = 0; i < 30; i++) {
            LinearLayout entry = initEntry();
            TextView name = initName();
            Button remove = initRemove();
            name.setText("Anna Nowak" + i);
            entry.addView(name);
            entry.addView(remove);
            contactsContainer.addView(entry);
        }
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
        name.setBackgroundColor(Color.parseColor("#52A0B7"));
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(20);
        return name;
    }

    Button initRemove() {
        Button remove =  new Button(getApplicationContext());
        LinearLayout.LayoutParams paramsRemove = new LinearLayout.LayoutParams(0, 120, 2);
        paramsRemove.leftMargin = 10;
        remove.setLayoutParams(paramsRemove);
        remove.setTypeface(Typeface.createFromAsset(getAssets(),
                "font/harabara.ttf"));
        remove.setGravity(Gravity.CENTER);
        remove.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E36161")));
        remove.setTextColor(Color.parseColor("#FFFFFF"));
        remove.setTextSize(15);
        remove.setText("Remove");
        return remove;
    }
}