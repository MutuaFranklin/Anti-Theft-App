package com.track.function;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.track.model.LocationModel;

public class SQLiteFunction extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "TrackDataBase";
    private static final int DATABASE_VERSION = 1;
    private SQLiteFunction sqLite;

    public SQLiteFunction(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new LocationModel().createLocationTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase writeDataBase() throws SQLException {
        if (sqLite == null)
            sqLite = SQLiteFunction.this;
        return sqLite.getWritableDatabase();
    }

    public SQLiteDatabase readDataBase() throws SQLException {
        if (sqLite == null)
            sqLite = SQLiteFunction.this;
        return sqLite.getReadableDatabase();
    }

    public void close() {
        sqLite.close();
    }
}
