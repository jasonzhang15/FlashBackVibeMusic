package com.android.flashbackmusic;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by raindrop on 3/15/18.
 */

public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;
    //...

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (intent == null) {
            return;
        }
        String errorMessage = "";
        // Get loc passed to this service through an extra
        LatLng location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Service not available";
            Log.e("ERROR", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid latitude/longitude used";
            Log.e("ERROR", errorMessage + ". " +
                    "Latitude = " + location.latitude +
                    ", Longitude = " +
                    location.longitude, illegalArgumentException);
        }
    }
}