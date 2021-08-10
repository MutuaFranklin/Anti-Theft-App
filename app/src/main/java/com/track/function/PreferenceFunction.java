package com.track.function;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class PreferenceFunction {

    public SharedPreferences sharedPreferences;
    private WeakReference<Context> contextWeakReference;
    private String PREF_INTRO_DONE = "pref_intro_done";
    private String PREF_EMAIL = "pref_email";
    private String PREF_PHONE_NUMBER = "pref_phone_number";
    private String PREF_USER_ID = "pref_user_id";
    private String PREF_USER_NAME = "pref_user_name";
    private String PREF_PASSWORD = "pref_password";
    private String PREF_LATITUDE = "pref_latitude";
    private String PREF_LONGITUDE = "pref_longitude";
    private String PREF_LOCATION = "pref_location";

    public PreferenceFunction(Context context) {
        contextWeakReference = new WeakReference<>(context);
        sharedPreferences = context.getSharedPreferences(SQLiteFunction.DATABASE_NAME, Context.MODE_PRIVATE);
    }

    public boolean isIntroDone() {
        return sharedPreferences.getBoolean(PREF_INTRO_DONE, false);
    }

    public void setIntroDone(boolean bool) {
        sharedPreferences.edit().putBoolean(PREF_INTRO_DONE, bool).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(PREF_USER_NAME, "");
    }

    public void setUserName(String string) {
        sharedPreferences.edit().putString(PREF_USER_NAME, string).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(PREF_EMAIL, "");
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(PREF_EMAIL, email).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(PREF_PHONE_NUMBER, "");
    }

    public void setPhoneNumber(String email) {
        sharedPreferences.edit().putString(PREF_PHONE_NUMBER, email).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(PREF_USER_ID, "");
    }

    public void setUserId(String email) {
        sharedPreferences.edit().putString(PREF_USER_ID, email).apply();
    }

    public String getLatitude() {
        return sharedPreferences.getString(PREF_LATITUDE, "0");
    }

    public void setLatitude(String string) {
        sharedPreferences.edit().putString(PREF_LATITUDE, string).apply();
    }

    public String getLongitude() {
        return sharedPreferences.getString(PREF_LONGITUDE, "0");
    }

    public void setLongitude(String string) {
        sharedPreferences.edit().putString(PREF_LONGITUDE, string).apply();
    }

    public String getLocation() {
        return sharedPreferences.getString(PREF_LOCATION, "");
    }

    public void setLocation(String string) {
        sharedPreferences.edit().putString(PREF_LOCATION, string).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PREF_PASSWORD, "");
    }

    public void setPassword(String string) {
        sharedPreferences.edit().putString(PREF_PASSWORD, string).apply();
    }

    public String getArrays(String newId) {
        return sharedPreferences.getString("pref_" + newId, "");
    }

    public void setArrays(String newId, String string) {
        sharedPreferences.edit().putString("pref_" + newId, string).apply();
    }

    private Context getContext() {
        return contextWeakReference.get();
    }


}
