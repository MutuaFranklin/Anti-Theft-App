package com.track.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.track.R;
import com.track.function.LocationFunction;
import com.track.function.PreferenceFunction;
import com.track.model.LocationModel;

import java.util.List;

public class MapActivity extends FragmentActivity {

    private Activity tActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        tActivity = MapActivity.this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback);
    }

    private GoogleMap map;
    private String latitude = "";
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            List<LocationModel> locationModelList = new LocationFunction(tActivity).fetchOnlineAllLocations();
            String prefLatitude = new PreferenceFunction(tActivity).getLatitude();
            String prefLongitude = new PreferenceFunction(tActivity).getLongitude();
            addMarker(new PreferenceFunction(tActivity).getUserName(), "Your here..", Double.valueOf(prefLatitude), Double.valueOf(prefLongitude));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(prefLatitude), Double.valueOf(prefLongitude)), 13.0f));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setTrafficEnabled(true);
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.add(new LatLng(Double.valueOf(prefLatitude), Double.valueOf(prefLongitude)));
            for (LocationModel locationModel : locationModelList) {
                if (!TextUtils.equals(latitude, locationModel.latitude)) {
                    polygonOptions.add(new LatLng(Double.valueOf(locationModel.latitude), Double.valueOf(locationModel.longitude)));
                    addMarker("", "", Double.valueOf(locationModel.latitude), Double.valueOf(locationModel.longitude));
                }
                latitude = locationModel.latitude;
            }
            map.addPolygon(polygonOptions).setStrokeColor(Color.RED);
        }
    };

    private void addMarker(String title, String snippet, double latitude, double longitude) {
        Marker marker = map.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_place))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .visible(true)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .flat(true)
                .title(title)
                .snippet(snippet)
                .position(new LatLng(latitude, longitude)));
        marker.showInfoWindow();
    }
}
