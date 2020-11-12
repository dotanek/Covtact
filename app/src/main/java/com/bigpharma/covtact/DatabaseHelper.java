package com.bigpharma.covtact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CONTACT_TABLE = "contacts";
    public static final String CONTACT_COLUMN_ID = "id";
    public static final String CONTACT_COLUMN_NAME = "name";
    public static final String CONTACT_COLUMN_DATE = "date";
    public static final String CONTACT_COLUMN_NOTE = "note";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "covtact.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE "+ CONTACT_TABLE + "(" +
                CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_COLUMN_NAME + " TEXT," +
                CONTACT_COLUMN_DATE + " DATE," +
                CONTACT_COLUMN_NOTE + " TEXT)";

        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // Methods for ContactModel

    public boolean addContact(ContactModel contactModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CONTACT_COLUMN_NAME, contactModel.getName());
        cv.put(CONTACT_COLUMN_DATE, contactModel.getDate().toString());
        cv.put(CONTACT_COLUMN_NOTE, contactModel.getNote());

        long success = db.insert(CONTACT_TABLE,null,cv);

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
                String contactDate = cursor.getString(2);
                String contactNote = cursor.getString(3);

                Date date = new Date();
                date.day = Integer.parseInt(contactDate.substring(0,2));
                date.month = Integer.parseInt(contactDate.substring(3,5));
                date.year = Integer.parseInt(contactDate.substring(6,10));

                contactModelList.add(new ContactModel(contactId,contactName,date,contactNote));

            } while(cursor.moveToNext());
        } else {
            throw(new Exception("Unable to read database."));
        }

        return contactModelList;
    }

    public boolean removeContact(int id) {
        String queryString = "DELETE FROM " + CONTACT_TABLE + " WHERE " + CONTACT_COLUMN_ID +"="+ id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) { // Returns false no matter the result, TODO find a way to check for success.
            return true;
        } else {
            return false;
        }
    }
}

class ContactModel {
    private int id;
    private String name;
    private Date date;
    private String note;

    public ContactModel(String name, Date date, String note) {
        this.name = name;
        this.date = date;
        this.note = note;
    }

    public ContactModel(int id, String name, Date date, String note) {
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

    public Date getDate() {
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
class PathModel {
    private int id;
    private java.sql.Date startDate;
    private java.sql.Date endDate;

    public PathModel(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "PathModel{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
class PathPointModel {
    private int id;
    private int pathPointIndex;
    private int pathId;
    private java.sql.Date date;
    private double longtitude;
    private double latitude;

    public PathPointModel(java.sql.Date date, double longtitude, double latitude) {
        this.date = date;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.pathPointIndex = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPathPointIndex() {
        return pathPointIndex;
    }

    public void setPathPointIndex(int pathPointIndex) {
        this.pathPointIndex = pathPointIndex;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "PathPointModel{" +
                "id=" + id +
                ", pathPointIndex=" + pathPointIndex +
                ", pathId=" + pathId +
                ", date=" + date +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }
}