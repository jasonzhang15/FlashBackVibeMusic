package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nataliepopescu on 2/17/18.
 */

public interface LocationInterface {

    double getLatitude();
    double getLongitude();
    LatLng getCurrentLocation();

}
