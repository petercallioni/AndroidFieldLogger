package com.callioni.peter.fieldlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    private static final String KEY_ROWID = "id";
    private static final String KEY_FIELD = "Field";
    private static final String KEY_TIME = "DateString";
    private static final String KEY_CONDUCTIVITY = "Conductivity";
    private static final String KEY_PH = "PH";
    private static final String KEY_MOISTURE = "Moisture";
    private static final String KEY_DISSOLVED_OXYGEN = "Dissolved_Oxygen";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "FieldLoggerDB";
    private static final String DATABASE_TABLE = "FieldData";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table FieldData (id integer primary key autoincrement, " +
            "Field text not null, DateString text not null, Conductivity float not null, " +
            " PH float not null, Moisture integer not null, Dissolved_Oxygen interger not null);";
    private final Context context;

    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DataBaseHelper(context);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {
        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            {
                try {
                    db.execSQL(DATABASE_CREATE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS FieldData");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    //insert a fields data into the table
    public long insertFieldData(String fieldName, String time, float conductivity, float ph, int moisture, int dissolvedOxygen) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FIELD, fieldName);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_CONDUCTIVITY, conductivity);
        initialValues.put(KEY_PH, ph);
        initialValues.put(KEY_MOISTURE, moisture);
        initialValues.put(KEY_DISSOLVED_OXYGEN, dissolvedOxygen);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //get all fields data from the table
    public Cursor getAllFields() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_FIELD, KEY_TIME, KEY_CONDUCTIVITY, KEY_PH, KEY_MOISTURE, KEY_DISSOLVED_OXYGEN}, null, null, null, null, null);
    }

    public void clearFieldData() {
        try {
            db.delete(DATABASE_TABLE, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
