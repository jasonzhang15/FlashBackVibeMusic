package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class SongPlay {

    String username;
    LatLng location;
    String time;
    Date date;

    public SongPlay(String username, LatLng location, String time, Date date) {
        this.username = username;
        this.location = location;
        this.time = time;
        this.date = date;
    }
}
