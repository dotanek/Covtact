package com.bigpharma.covtact.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

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
}