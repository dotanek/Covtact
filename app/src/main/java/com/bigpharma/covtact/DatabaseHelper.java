package com.bigpharma.covtact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.bigpharma.covtact.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String NAME_DB_FILE = "covtact.db";
    //PathModel
    public static final String PATH_TABLE = "path";
    public static final String PATH_COLUMN_ID = "id";
    public static final String PATH_COLUMN_DEVICE_OWNER = "deviceOwner";
    public static final String PATH_COLUMN_START_DATE = "startDate";
    public static final String PATH_COLUMN_END_DATE = "endDate";
    //PathPointModel
    public static final String PATH_POINT_TABLE = "pathPoint";
    public static final String PATH_POINT_COLUMN_ID = "id";
    public static final String PATH_POINT_COLUMN_PATH_POINT_INDEX = "pathPointIndex";
    public static final String PATH_POINT_COLUMN_PATH_ID = "pathId";
    public static final String PATH_POINT_COLUMN_DATE = "date";
    public static final String PATH_POINT_COLUMN_LONGTITUDE = "longtitude";
    public static final String PATH_POINT_COLUMN_LATITUDE = "latitude";
    //ContactModel
    public static final String CONTACT_TABLE = "contacts";
    public static final String CONTACT_COLUMN_ID = "id";
    public static final String CONTACT_COLUMN_NAME = "name";
    public static final String CONTACT_COLUMN_DATE = "date";
    public static final String CONTACT_COLUMN_NOTE = "note";

    public DatabaseHelper(@Nullable Context context) {
        super(context, NAME_DB_FILE, null, 1);
        pathDatabaseHelper = new PathDatabaseHelper(this);
    }

    private PathDatabaseHelper pathDatabaseHelper;

    public PathDatabaseHelper getPathDatabaseHelper() {
        return pathDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE "+ CONTACT_TABLE + "(" +
                CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_COLUMN_NAME + " TEXT," +
                CONTACT_COLUMN_DATE + " DATE," +
                CONTACT_COLUMN_NOTE + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
        pathDatabaseHelper.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        pathDatabaseHelper.onUpgrade(sqLiteDatabase,i,i1);
    }

    // Methods for ContactModel

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

class ContactModel {
    private int id;
    private String name;
    private java.util.Date date;
    private String note;

    public ContactModel(String name, java.util.Date date, String note) {
        this.name = name;
        this.date = date;
        this.note = note;
    }

    public ContactModel(int id, String name, java.util.Date date, String note) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
    }
}