package com.android.flashbackmusic;

import java.util.Date;

/**
 * Created by valentinadibs on 3/9/18.
 */

public class SongPlay {

    String songname;
    String username;
    String location;
    String time;
    Date date;

    public SongPlay(String songname, String username, String location, String time, Date date) {
        this.songname = songname;
        this.username = username;
        this.location = location;
        this.time = time;
        this.date = date;
    }
}
