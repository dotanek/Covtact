package com.bigpharma.covtact.DataType;

import java.util.Calendar;

public class ContactDate {
    public int day;
    public int month;
    public int year;

    public ContactDate() {
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }

    public String toString() {
        String dateStr = ((day > 9) ? Integer.toString(day) : "0" + day);
        dateStr += "/" + ((month+1 > 9) ? month+1 : "0" + (month+1));
        dateStr += "/" + year;
        return dateStr;
    }
}