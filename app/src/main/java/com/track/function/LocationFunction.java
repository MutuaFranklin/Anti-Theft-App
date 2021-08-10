package com.track.function;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.track.model.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LocationFunction {
    private WeakReference<Context> contextWeakReference;

    public LocationFunction(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    private Context getContext() {
        return contextWeakReference.get();
    }

    private LocationModel parseJsonLocations(JSONObject c) throws JSONException {
        LocationModel locationModel = new LocationModel();
        locationModel.location_id = c.getString(locationModel.LOCATION_ID);
        locationModel.user_id = c.getString(locationModel.USER_ID);
        locationModel.latitude = c.getString(locationModel.LATITUDE);
        locationModel.longitude = c.getString(locationModel.LONGITUDE);
        locationModel.location = c.getString(locationModel.LOCATION);
        locationModel.description = c.getString(locationModel.DESCRIPTION);
        locationModel.updated_at = c.getString(locationModel.UPDATED_AT);
        locationModel.created_at = c.getString(locationModel.CREATED_AT);
        return locationModel;
    }

    public List<LocationModel> fetchOnlineAllLocations() {
        List<LocationModel> locationModelList = new ArrayList<>();
        String jsonArrays = new PreferenceFunction(getContext()).getArrays(UtilityFunction.locations);

        try {
            JSONObject jsonObject = new JSONObject(jsonArrays);
            JSONArray jsonArray = jsonObject.getJSONArray(UtilityFunction.locations);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject c = jsonArray.getJSONObject(i);
                LocationModel loopTasksModel = parseJsonLocations(c);
                locationModelList.add(loopTasksModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationModelList;
    }

    public void insertOfflineLocation(String locationId, String description) {
        LocationModel locationModel = new LocationModel();
        locationModel.location_id = locationId;
        locationModel.user_id = new PreferenceFunction(getContext()).getUserId();
        locationModel.latitude = new PreferenceFunction(getContext()).getLatitude();
        locationModel.longitude = new PreferenceFunction(getContext()).getLongitude();
        locationModel.location = new PreferenceFunction(getContext()).getLocation();
        locationModel.description = description;
        insertLocation(locationModel);
    }

    public List<LocationModel> fetchOfflineAllLocations() throws SQLException {
        SQLiteDatabase db = null;
        List<LocationModel> locationModelList = new ArrayList<>();

        Cursor cursor = null;

        try {
            db = new SQLiteFunction(getContext()).readDataBase();
            String query = "SELECT * FROM " + new LocationModel().LOCATION_MODEL + " ORDER BY " + new LocationModel().CREATED_AT + " DESC";
            cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {

                cursor.moveToFirst();
                do {
                    locationModelList.add(parseLocations(cursor));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();

            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        } finally {
            if (cursor != null)
                cursor.close();

            if (db != null)
                db.close();
        }

        return locationModelList;
    }

    private LocationModel parseLocations(Cursor cursor) {
        LocationModel locationModel = new LocationModel();
        locationModel.id = cursor.getInt(cursor.getColumnIndex(locationModel.ID));
        locationModel.location_id = cursor.getString(cursor.getColumnIndex(locationModel.LOCATION_ID));
        locationModel.user_id = cursor.getString(cursor.getColumnIndex(locationModel.USER_ID));
        locationModel.latitude = cursor.getString(cursor.getColumnIndex(locationModel.LATITUDE));
        locationModel.longitude = cursor.getString(cursor.getColumnIndex(locationModel.LONGITUDE));
        locationModel.location = cursor.getString(cursor.getColumnIndex(locationModel.LOCATION));
        locationModel.description = cursor.getString(cursor.getColumnIndex(locationModel.DESCRIPTION));
        locationModel.updated_at = cursor.getString(cursor.getColumnIndex(locationModel.UPDATED_AT));
        locationModel.created_at = cursor.getString(cursor.getColumnIndex(locationModel.CREATED_AT));
        return locationModel;
    }

    private void insertLocation(LocationModel locationModel) {
        SQLiteDatabase db = new SQLiteFunction(getContext()).writeDataBase();
        ContentValues values = new ContentValues();
        values.put(locationModel.LOCATION_ID, locationModel.location_id);
        values.put(locationModel.USER_ID, locationModel.user_id);
        values.put(locationModel.LATITUDE, locationModel.latitude);
        values.put(locationModel.LONGITUDE, locationModel.longitude);
        values.put(locationModel.LOCATION, locationModel.location);
        values.put(locationModel.DESCRIPTION, locationModel.description);
        values.put(locationModel.UPDATED_AT, new FeaturesFunction(getContext()).getTimeDate());
        values.put(locationModel.CREATED_AT, new FeaturesFunction(getContext()).getTimeDate());
        db.insert(locationModel.LOCATION_MODEL, null, values);
    }

    public void deleteLocation(String locationId) {
        SQLiteDatabase db = new SQLiteFunction(getContext()).writeDataBase();
        String[] select = new String[]{String.valueOf(locationId)};
        db.delete(new LocationModel().LOCATION_MODEL, new LocationModel().LOCATION_ID + " LIKE ? ", select);
        db.close();
    }
}
