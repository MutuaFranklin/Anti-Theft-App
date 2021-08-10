package com.track.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.track.R;
import com.track.function.AddressFunction;
import com.track.function.AlarmFunction;
import com.track.function.FeaturesFunction;
import com.track.function.GpsLocationFunction;
import com.track.function.PreferenceFunction;
import com.track.interfaces.OnPostPosition;
import com.track.model.AddressedModel;

public class HomeActivity extends AppCompatActivity {

    private Activity tActivity;
    private TextView emailText, useNameText, deviceNameText, modelNumberText, imeiNumberText, phoneNumberText, locationText;
    private Menu barMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tActivity = HomeActivity.this;

        useNameText = findViewById(R.id.user_name_textView);
        emailText = findViewById(R.id.email_textView);

        deviceNameText = findViewById(R.id.device_name_textView);
        modelNumberText = findViewById(R.id.model_number_textView);
        imeiNumberText = findViewById(R.id.imei_number_textView);
        phoneNumberText = findViewById(R.id.phone_number_textView);
        locationText = findViewById(R.id.location_textView);

        useNameText.setText(new PreferenceFunction(tActivity).getUserName());
        emailText.setText(new PreferenceFunction(tActivity).getEmail());
        deviceNameText.setText(new FeaturesFunction(tActivity).getDeviceName());
        modelNumberText.setText(new FeaturesFunction(tActivity).getModelNumber());
        imeiNumberText.setText(new FeaturesFunction(tActivity).getImei());
        phoneNumberText.setText(new FeaturesFunction(tActivity).getSimPhoneNumber());
        locationText.setText("Searching...");


        if (!new PreferenceFunction(tActivity).isIntroDone()) {
            startActivity(new Intent(tActivity, LoginActivity.class));
            finish();
        } else {
            new AlarmFunction(tActivity).startRepeatingAlarm();

            GpsLocationFunction gpsLocationFunction = new GpsLocationFunction(tActivity);
            gpsLocationFunction.locationStart();
            gpsLocationFunction.setOnPostPosition(new OnPostPosition() {
                @Override
                public void onPostPosition(final double latitude, final double longitude) {
                    new PreferenceFunction(tActivity).setLatitude(String.valueOf(latitude));
                    new PreferenceFunction(tActivity).setLongitude(String.valueOf(longitude));
                    locationText.setText(new StringBuilder()
                            .append("Searching address...")
                            .append("\n").append("Lat : ")
                            .append(latitude).append(" \n")
                            .append("Long : ")
                            .append(longitude)
                    );
                    final AddressedModel addressedModel = new AddressFunction(tActivity).geoCoderUtil(latitude, latitude);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            locationText.setText(new StringBuilder()
                                    .append("Address : ")
                                    .append(addressedModel.getAddress())
                                    .append("\n").append("Lat : ")
                                    .append(latitude).append(" \n")
                                    .append("Long : ")
                                    .append(longitude)
                            );
                        }
                    }, 1000);
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (showPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 3000);
        }
    }

    private boolean showPermission() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the nMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_home, menu);
        barMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_bar_mapping:
                startActivity(new Intent(tActivity, MapActivity.class));
                break;

            case R.id.action_bar_logOut:
                new PreferenceFunction(tActivity).setUserId(null);
                new PreferenceFunction(tActivity).setPhoneNumber(null);
                new PreferenceFunction(tActivity).setIntroDone(false);
                recreate();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
