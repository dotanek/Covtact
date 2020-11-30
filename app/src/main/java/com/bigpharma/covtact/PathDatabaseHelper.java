package com.bigpharma.covtact;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.util.Pair;

import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;
import com.bigpharma.covtact.util.Util;

import java.util.Date;


public class PathDatabaseHelper {
    DatabaseHelper databaseHelper;

    public PathDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void onCreate(SQLiteDatabase db) {
        String createTableStatementPath = String.format("CREATE TABLE %s (", DatabaseHelper.PATH_TABLE) +
                String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ", DatabaseHelper.PATH_COLUMN_ID) +
                String.format("%s INTEGER,", DatabaseHelper.PATH_COLUMN_DEVICE_OWNER) +
                String.format("%s DATE,", DatabaseHelper.PATH_COLUMN_START_DATE) +
                String.format("%s DATE", DatabaseHelper.PATH_COLUMN_END_DATE) +
                ")";
        db.execSQL(createTableStatementPath);
        String createTableStatementPathPoint = String.format("CREATE TABLE %s (", DatabaseHelper.PATH_POINT_TABLE) +
                String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ", DatabaseHelper.PATH_POINT_COLUMN_ID) +
                String.format("%s INTEGER, ", DatabaseHelper.PATH_POINT_COLUMN_PATH_POINT_INDEX) +
                String.format("%s INTEGER,", DatabaseHelper.PATH_POINT_COLUMN_PATH_ID) +
                String.format("%s DATE,", DatabaseHelper.PATH_POINT_COLUMN_DATE) +
                String.format("%s DOUBLE,", DatabaseHelper.PATH_POINT_COLUMN_LONGTITUDE) +
                String.format("%s DOUBLE", DatabaseHelper.PATH_POINT_COLUMN_LATITUDE) +
                ")";
        db.execSQL(createTableStatementPathPoint);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public PathModel addPath(PathModel pathModel) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PATH_COLUMN_DEVICE_OWNER,pathModel.isDeviceOwner() ? 1 : 0);
        cv.put(DatabaseHelper.PATH_COLUMN_START_DATE, Util.dateToSqliteString(pathModel.getStartDate()));
        cv.put(DatabaseHelper.PATH_COLUMN_END_DATE,Util.dateToSqliteString(pathModel.getEndDate()));
        long rowid = db.insert(DatabaseHelper.PATH_TABLE,null,cv);
        if(rowid == -1) {
            return null;
        }
        PathModel result = getPathById(rowid);
        return result;
    }

    public PathModel updatePath(PathModel pathModel) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if(pathModel.getId() == null) {
            return null;
        }
        PathModel pathModelInDb = getPathById(pathModel.getId());
        pathModelInDb.setEndDate(pathModel.getEndDate());
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PATH_COLUMN_DEVICE_OWNER,pathModelInDb.isDeviceOwner() ? 1 : 0);
        cv.put(DatabaseHelper.PATH_COLUMN_START_DATE, Util.dateToSqliteString(pathModelInDb.getStartDate()));
        cv.put(DatabaseHelper.PATH_COLUMN_END_DATE,Util.dateToSqliteString(pathModelInDb.getEndDate()));
        db.update(DatabaseHelper.PATH_TABLE,cv,"id == ?", new String[]{String.format("%d", pathModelInDb.getId())});
        return getPathById(pathModelInDb.getId());
    }

    public PathModel getLastestOwnedPath() {//TODO change name of function to getOwnedPathWithMaxId
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String queryString = "SELECT * FROM "+ DatabaseHelper.PATH_TABLE +
                " ORDER BY "+ DatabaseHelper.PATH_COLUMN_ID + " DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_ID));
            boolean deviceOwner = (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_DEVICE_OWNER)) == 1);
            String startDateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_START_DATE));
            String endDateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_END_DATE));
            Date startDate = Util.sqliteStringToDate(startDateStr);
            Date endDate = Util.sqliteStringToDate(endDateStr);
            PathModel pathModelNew = new PathModel(startDate);
            pathModelNew.setId(id);
            pathModelNew.setDeviceOwner(deviceOwner);
            pathModelNew.setStartDate(startDate);
            pathModelNew.setEndDate(endDate);
            return pathModelNew;
        }
        return null;
    }

    public PathModel getPathById(long index) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.PATH_TABLE);
        queryBuilder.appendWhere(DatabaseHelper.PATH_COLUMN_ID + " = ?");
        String[] args = {String.format("%d",index)};
        String queryString = queryBuilder.buildQuery(null,null,null,null,null,"1");
        Cursor cursor = db.rawQuery(queryString,args);
        if(cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_ID));
            boolean deviceOwner = (cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_DEVICE_OWNER)) == 1);
            String startDateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_START_DATE));
            String endDateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_COLUMN_END_DATE));
            Date startDate = Util.sqliteStringToDate(startDateStr);
            Date endDate = Util.sqliteStringToDate(endDateStr);
            PathModel pathModelNew = new PathModel(startDate);
            pathModelNew.setId(id);
            pathModelNew.setDeviceOwner(deviceOwner);
            pathModelNew.setStartDate(startDate);
            pathModelNew.setEndDate(endDate);
            return pathModelNew;
        }
        return null;
    }

    public PathPointModel getPathPointById(long index) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.PATH_POINT_TABLE);
        queryBuilder.appendWhere(DatabaseHelper.PATH_POINT_COLUMN_ID + " = ?");
        String[] args = {String.format("%d",index)};
        String queryString = queryBuilder.buildQuery(null,null,null,null,null,"1");
        Cursor cursor = db.rawQuery(queryString,args);
        if(cursor.moveToFirst()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_ID));
            Integer pathPointIndex = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_PATH_POINT_INDEX));
            Integer pathId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_PATH_ID));
            String dateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_DATE));
            Date date = Util.sqliteStringToDate(dateStr);
            double longtitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_LONGTITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_LATITUDE));
            PathPointModel pathModelNew = new PathPointModel(date,longtitude,latitude);
            pathModelNew.setId(id);
            pathModelNew.setPathPointIndex(pathPointIndex);
            pathModelNew.setPathId(pathId);
            return pathModelNew;
        }
        return null;
    }

    public PathPointModel getLastPathPointInPath(PathModel pathModel) { //TODO change name of function to getPathPointInPathWithMaxId
        if(pathModel.getId() == null) {
            return null;
        }
        PathModel pathPointModelInDb = getPathById(pathModel.getId());
        if(pathPointModelInDb.getId() == null) {
            return null; //When path not exist in database
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String queryString = "SELECT * FROM "+ DatabaseHelper.PATH_POINT_TABLE +
                " WHERE "+ DatabaseHelper.PATH_POINT_COLUMN_PATH_ID + " = " + String.valueOf(pathPointModelInDb.getId()) +
                " ORDER BY "+ DatabaseHelper.PATH_POINT_COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_ID));
            Integer pathPointIndex = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_PATH_POINT_INDEX));
            Integer pathId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_PATH_ID));
            String dateStr = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_DATE));
            Date date = Util.sqliteStringToDate(dateStr);
            double longtitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_LONGTITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.PATH_POINT_COLUMN_LATITUDE));
            PathPointModel pathModelNew = new PathPointModel(date,longtitude,latitude);
            pathModelNew.setId(id);
            pathModelNew.setPathPointIndex(pathPointIndex);
            pathModelNew.setPathId(pathId);
            return pathModelNew;
        }
        return null;
    }
    public Pair<PathModel,PathPointModel> addPathPointToPath(PathModel pathModel, PathPointModel pathPointModel) {
        if(pathModel.getId() == null) {
            Log.i("addPathPointToPath","pathMode.getId() == null");
            return new Pair<PathModel,PathPointModel>(null,null);
        }
        PathModel pathModelInDb = getPathById(pathModel.getId());
        if(pathModelInDb == null) {
            Log.i("addPathPointToPath","pathModelInDb == null");
            return new Pair<PathModel,PathPointModel>(null,null); // When path not exist in database
        }
        PathPointModel prevPathPoint = getLastPathPointInPath(pathModelInDb);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int pathPointIndex = 1;
        if(prevPathPoint != null) {
            pathPointIndex = 1 + prevPathPoint.getPathPointIndex();
        }
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.PATH_POINT_COLUMN_PATH_POINT_INDEX,pathPointIndex);
        cv.put(DatabaseHelper.PATH_POINT_COLUMN_PATH_ID,pathModelInDb.getId());
        cv.put(DatabaseHelper.PATH_POINT_COLUMN_DATE, Util.dateToSqliteString(pathPointModel.getDate()));
        cv.put(DatabaseHelper.PATH_POINT_COLUMN_LONGTITUDE, pathPointModel.getLongtitude());
        cv.put(DatabaseHelper.PATH_POINT_COLUMN_LATITUDE, pathPointModel.getLatitude());
        long rowid = db.insert(DatabaseHelper.PATH_POINT_TABLE,null,cv);
        if(rowid == -1) {
            Log.i("addPathPointToPath","rowid == -1");
            return new Pair<PathModel,PathPointModel>(null,null);
        }
        pathModelInDb.setEndDate(pathPointModel.getDate());
        pathModelInDb = updatePath(pathModelInDb);
        Pair<PathModel,PathPointModel> result = new Pair<>(pathModelInDb, getPathPointById(rowid));
        return result;
    }
}
