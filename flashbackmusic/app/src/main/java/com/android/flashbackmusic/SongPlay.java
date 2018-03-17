package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

public class SongPlay {

    String username;
    Double latitude;
    Double longitude;
    String time;
    Date date;

    public SongPlay(){}

    public SongPlay(String username, LatLng location, String time, Date date) {
        this.username = username;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.time = time;
        this.date = date;
    }
}
