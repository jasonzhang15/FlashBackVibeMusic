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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by nataliepopescu on 2/17/18.
 */

public class LocationAdapter implements LocationInterface {

    // The entry point to the Fused Location Provider.

    private String locationProvider;
    private LocationManager locationManager;

    // Compose Location
    private Location location;
    private Context context;

    LocationAdapter() { //FusedLocationProviderClient client) {
        locationProvider = LocationManager.GPS_PROVIDER;
        location = new Location(locationProvider);
    }

    public double getLatitude() { return location.getLatitude(); }

    public double getLongitude() { return location.getLongitude(); }

    public LatLng getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        location = locationManager.getLastKnownLocation(locationProvider);
        Log.v("CURLOC: ", "" + location);
        return new LatLng(location.getLatitude(), location.getLongitude());

    }

    void establishLocationPermission(Context context, Activity activity) {

        if (context == null || activity == null) return;
        this.context = context;
        //this.activity = activity;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        } else {
            startLocationUpdates();
        }
    }

    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();

                } else {
                    Toast.makeText(context, "Flashback Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location curLocation) {
                location = curLocation;
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

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

}
