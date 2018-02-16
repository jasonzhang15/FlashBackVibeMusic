package com.android.flashbackmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;

/**
 * Stores current user information
 */

public class User {
    LatLng location;
    DayOfWeek day;
    //TODO: Add constants to make this bearable
    int timeOfDay;

    //TODO: Add getters/setters

    public LatLng getLocation(Context context, Activity activity) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            Log.d("test1","ins");
            return null;
        } else {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.GPS_PROVIDER;
            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            Location locations = locationManager.getLastKnownLocation(locationProvider);
            Log.d("location: ", "lat: " + locations.getLatitude() + " lng: " + locations.getLongitude());
            return new LatLng(locations.getLatitude(), locations.getLongitude());
        }

    }

}
