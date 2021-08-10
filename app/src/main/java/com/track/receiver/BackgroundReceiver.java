package com.track.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;

import com.track.function.FeaturesFunction;
import com.track.function.GpsLocationFunction;
import com.track.function.LocationFunction;
import com.track.function.NetworkingFunction;
import com.track.function.PreferenceFunction;
import com.track.interfaces.OnPostPosition;
import com.track.model.LocationModel;

import java.util.List;

public class BackgroundReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String uploadedNumber = new PreferenceFunction(context).getPhoneNumber();
        String simNumber = new FeaturesFunction(context).getSimPhoneNumber();
        final String userId = new PreferenceFunction(context).getUserId();
        if (!TextUtils.equals(uploadedNumber, simNumber)) {
            String description = userId + "'s Sim card has changed from " + uploadedNumber + " to ( " + simNumber + " )";
            String locationId = String.valueOf(System.currentTimeMillis());
            new LocationFunction(context).insertOfflineLocation(locationId, description);
            new NetworkingFunction(context).postChanges(locationId, description);
        }


        final GpsLocationFunction gpsLocationFunction = new GpsLocationFunction(context);
        gpsLocationFunction.locationStart();
        gpsLocationFunction.setOnPostPosition(new OnPostPosition() {
            @Override
            public void onPostPosition(double latitude, double longitude) {
                checkLocation(context, userId, latitude, longitude);
                gpsLocationFunction.locationStop();
            }
        });

        List<LocationModel> locationModelList = new LocationFunction(context).fetchOfflineAllLocations();
        for (LocationModel locationModel : locationModelList) {
            delayPostAuto(context, locationModel);
        }

        new android.os.Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new NetworkingFunction(context).fetchLocation();
            }
        }, 2000);
    }

    private static void delayPostAuto(final Context context, final LocationModel locationModel) {
        new android.os.Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new NetworkingFunction(context).postChanges(locationModel.location_id, locationModel.description);
            }
        }, 1000);
    }

    private void checkLocation(Context context, String userId, double latitude, double longitude) {
        String prefLatitude = new PreferenceFunction(context).getLatitude();
        String prefLongitude = new PreferenceFunction(context).getLongitude();

        if (TextUtils.isEmpty(prefLatitude) && TextUtils.isEmpty(prefLongitude)) {
            new PreferenceFunction(context).setLatitude(String.valueOf(latitude));
            new PreferenceFunction(context).setLongitude(String.valueOf(longitude));
        } else if (!TextUtils.equals(String.valueOf(latitude), prefLatitude) && !TextUtils.equals(String.valueOf(longitude), prefLongitude)) {
            String locationId = String.valueOf(System.currentTimeMillis());
            String description = userId + "'s location has changed from " + prefLatitude + "," + prefLongitude + " to " + latitude + "," + longitude;
            new LocationFunction(context).insertOfflineLocation(locationId, description);
            new NetworkingFunction(context).postChanges(locationId, description);
            new PreferenceFunction(context).setLatitude(String.valueOf(latitude));
            new PreferenceFunction(context).setLongitude(String.valueOf(longitude));
        }
    }

}
