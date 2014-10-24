package com.iosharp.android.whereareyou.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.iosharp.android.whereareyou.model.Point;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PointDB";

    // Points table name
    private static final String TABLE_POINTS = "points";

    // Points table column names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static final String[] COLUMNS = {KEY_ID, KEY_LATITUDE, KEY_LONGITUDE};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_POINT_TABLE = "CREATE TABLE points ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date INTEGER, " +
                "latitude REAL, " +
                "longitude REAL )";

        sqLiteDatabase.execSQL(CREATE_POINT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS points");

        this.onCreate(sqLiteDatabase);
    }

    public void addPoint(Point point) {
        Log.d("addPoint", point.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, point.getLatitude());
        values.put(KEY_LONGITUDE, point.getLongitude());

        db.insert(TABLE_POINTS, null, values);

        db.close();
    }

    public Point getPoint(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POINTS,          // table
                COLUMNS,                                // column names
                " id = ?",                              // selections
                new String[] { String.valueOf(id)},     // selection args
                null,                                   // group by
                null,                                   // having
                null,                                   // order by
                null);                                  // limit

        // If we get results, get the first
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Point point = new Point();
        point.setId(Integer.parseInt(cursor.getString(0)));
        point.setLatitude(Double.parseDouble(cursor.getString(1)));
        point.setLongitude(Double.parseDouble(cursor.getString(2)));

        Log.d("getPoint("+ id + ")", point.toString());

        return point;
    }

    public List<Point> getAllPoints() {
        List<Point> points = new LinkedList<Point>();

        String query = "SELECT * FROM " + TABLE_POINTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Point point = null;
        if (cursor.moveToFirst()) {
            do {
                point = new Point();
                point.setId(Integer.parseInt(cursor.getString(0)));
                point.setLatitude(Double.parseDouble(cursor.getString(1)));
                point.setLongitude(Double.parseDouble(cursor.getString(2)));

                points.add(point);
            } while (cursor.moveToNext());
        }

        Log.d("getAllPoints()", points.toString());

        return points;
    }

    public int updatePoint(Point point) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("latitude", point.getLatitude());
        values.put("longitude", point.getLongitude());

        int i = db.update(TABLE_POINTS,                             // table
                values,                                             // column/value
                KEY_ID + " = ?",                                    // selections
                new String[] { String.valueOf(point.getId()) });    // selection

        db.close();

        return i;
    }

    public void deletePoint(Point point) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_POINTS,                                     // table
                KEY_ID + " = ?",                                    // selections
                new String[] { String.valueOf(point.getId()) });    // selection args

        db.close();

        Log.d("deletePoint", point.toString());
    }
}
