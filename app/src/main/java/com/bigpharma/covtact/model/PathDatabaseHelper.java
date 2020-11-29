package com.bigpharma.covtact.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PathDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE = "contacts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DEVICE_OWNER = "deviceOwner";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";
    public PathDatabaseHelper(@Nullable Context context) {
        super(context, DatabaseHelper.NAME_DB_FILE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStarement = String.format("CREATE TABLE %s (",DatabaseHelper.NAME_DB_FILE) +
                String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ", COLUMN_ID) +
                String.format("%s INTEGER,",COLUMN_DEVICE_OWNER) +
                String.format("%s DATE,",COLUMN_START_DATE) +
                String.format("%s DATE,",COLUMN_END_DATE) +
                ")";
        db.execSQL(createTableStarement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Pair<Boolean, PathModel> addPath(PathModel pathModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DEVICE_OWNER,pathModel.isDeviceOwner() ? 1 : 0);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        cv.put(COLUMN_START_DATE,dateFormat.format(pathModel.getStartDate()));
        cv.put(COLUMN_END_DATE,dateFormat.format(pathModel.getEndDate()));
        long insertedRow = db.insert(TABLE,null,cv);

        String queryString = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 1",TABLE,COLUMN_ID);
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            boolean deviceOwner = (cursor.getInt(cursor.getColumnIndex(COLUMN_DEVICE_OWNER)) == 1);
            String startDateStr = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
            String endDateStr = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
            Date startDate = pathModel.getStartDate();
            Date endDate = pathModel.getEndDate();
            try {
                startDate = dateFormat.parse(startDateStr);
                endDate = dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PathModel pathModelNew = new PathModel(startDate);
            pathModelNew.setId(id);
            pathModelNew.setDeviceOwner(deviceOwner);
            pathModelNew.setStartDate(startDate);
            pathModelNew.setEndDate(endDate);
            return  new Pair<Boolean, PathModel>(true,pathModelNew);
        }
        return new Pair<Boolean, PathModel>(false,null);
    }

}
