package com.android.flashbackmusic;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nataliepopescu on 2/18/18.
 */

public class LocationMock implements LocationInterface {

    LocationAdapter locationAdapter;

    LocationMock() {
        locationAdapter = new LocationAdapter();
    }

    public double getLatitude() { return locationAdapter.getLatitude(); }

    public double getLongitude() { return locationAdapter.getLongitude(); }

    public LatLng getCurrentLocation() { return locationAdapter.getCurrentLocation(); }

    public void setLatitude(double latitude) { locationAdapter.setLatitude(latitude); }

    public void setLongitude(double longitude) { locationAdapter.setLongitude(longitude); }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) { ; }

    @Override
    public void establishLocationPermission(Context context, Activity activity) { ; }
}
