package com.android.flashbackmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by nataliepopescu on 2/17/18.
 */

public class LocationAdapter implements LocationInterface {

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationClient;

    // Compose Location
    private Location location;
    private Context context;
    private Activity activity;
    private Task<Location> task;

    LocationAdapter(FusedLocationProviderClient client) {
        mFusedLocationClient = client;
        String locationProvider = LocationManager.GPS_PROVIDER;
        location = new Location(locationProvider);
    }

    public double getLatitude() { return location.getLatitude(); }

    public double getLongitude() { return location.getLongitude(); }

    private void getCurrentLocation(Context context) {
        if (context == null) return;

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        task = mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location curLocation) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            location = task.getResult();
                            Log.d("Location", " " + location);
                        }
                    }
                });
    }

    void establishLocationPermission(Context context, Activity activity) {

        if (context == null || activity == null) return;
        this.context = context;
        this.activity = activity;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
    }

    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(context, "Flashback Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
