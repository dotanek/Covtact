package com.bigpharma.covtact.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.bigpharma.covtact.model.ContactModel;
import com.bigpharma.covtact.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ContactDatabaseHelper extends DatabaseHelper {

    public ContactDatabaseHelper(@Nullable Context context) {
        super(context);
    }

    public boolean addContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CONTACT_COLUMN_NAME, contactModel.getName());
        cv.put(CONTACT_COLUMN_DATE, Util.dateToSqliteString(contactModel.getDate()));
        cv.put(CONTACT_COLUMN_NOTE, contactModel.getNote());

        long success = db.insert(CONTACT_TABLE,null,cv);

        return success != -1;
    }

    public boolean updateContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CONTACT_COLUMN_NAME, contactModel.getName());
        cv.put(CONTACT_COLUMN_DATE, Util.dateToSqliteString(contactModel.getDate()));
        cv.put(CONTACT_COLUMN_NOTE, contactModel.getNote());

        long success = db.update(CONTACT_TABLE,cv,"id=?",new String[]{Integer.toString(contactModel.getId())});

        return success != -1;
    }

    public List<ContactModel> getContacts() throws Exception {
        List<ContactModel> contactModelList = new ArrayList<ContactModel>();

        String queryString = "SELECT * FROM " + CONTACT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int contactId = cursor.getInt(0);
                String contactName = cursor.getString(1);
                java.util.Date contactDate = Util.sqliteStringToDate(cursor.getString(2));
                String contactNote = cursor.getString(3);

                contactModelList.add(new ContactModel(contactId,contactName,contactDate,contactNote));

            } while(cursor.moveToNext());
        } else {
            //throw(new Exception("Unable to read database.")); Happens also when there is no contacts.
        }

        db.close();
        return contactModelList;
    }

    public boolean removeContact(int id) {
        String queryString = "DELETE FROM " + CONTACT_TABLE + " WHERE " + CONTACT_COLUMN_ID +"="+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) { // Returns false no matter the result, TODO find a way to check for success and failure.
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public int removeContactsBeforeDate(java.util.Date date) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(CONTACT_TABLE,"Date(" + CONTACT_COLUMN_DATE + ")" + "< DATE(?)", new String[]{Util.dateToSqliteString(date)});
        db.close();
        return result;
    }
}
