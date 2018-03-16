package com.android.flashbackmusic;

public class SongPlay {

    String songname;
    String username;
    String location;
    String time;
    Time date;

    public SongPlay(String songname, String username, String location, String time, Time date) {
        this.songname = songname;
        this.username = username;
        this.location = location;
        this.time = time;
        this.date = date;
    }
}
