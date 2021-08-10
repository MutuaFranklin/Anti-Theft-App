package com.track.model;

import android.database.sqlite.SQLiteDatabase;

public class LocationModel {

    public int id;
    public String location_id;
    public String user_id;
    public String latitude = "0";
    public String longitude = "0";
    public String location;
    public String description;
    public String updated_at;
    public String created_at;

    public String LOCATION_MODEL = "LocationModel";
    public String ID = "id";
    public String LOCATION_ID = "location_id";
    public String USER_ID = "user_id";
    public String LATITUDE = "latitude";
    public String LONGITUDE = "longitude";
    public String LOCATION = "location";
    public String DESCRIPTION = "description";
    public String UPDATED_AT = "updated_at";
    public String CREATED_AT = "created_at";

    public void createLocationTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LOCATION_MODEL + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , " +
                LOCATION_ID + " VARCHAR NOT NULL UNIQUE , " +
                USER_ID + " TEXT, " +
                LATITUDE + " TEXT, " +
                LONGITUDE + " TEXT,  " +
                LOCATION + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                UPDATED_AT + " DATETIME, " +
                CREATED_AT + " DATETIME)");
    }
}
