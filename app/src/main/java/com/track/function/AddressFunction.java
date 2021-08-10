package com.track.function;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.track.model.AddressedModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressFunction {

    private Context tContext;

    public AddressFunction(Context context) {
        tContext = context;
    }

    public AddressedModel geoCoderUtil(double latitudes, double longitudes) {
        AddressedModel addressedModel = new AddressedModel();
        Geocoder geocoder = new Geocoder(tContext, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitudes, longitudes, 1);
            addressedModel.setAddress(addresses.get(0).getAddressLine(0));
            addressedModel.setCity(addresses.get(0).getAddressLine(1));
            addressedModel.setCountry(addresses.get(0).getAddressLine(2));
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return addressedModel;
    }
}
