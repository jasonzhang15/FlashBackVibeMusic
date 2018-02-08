package com.android.flashbackmusic;

import java.io.FileDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

/**
 * Stores information about a single song
 */

class Song {

    private FileDescriptor filePath;
    private String title;
    private String artist;
    private Album album;
    private boolean favorited;
    private boolean disliked;
    private ArrayList<LatLng> locations;
    private Date lastPlayedTime;
    private boolean[] timesOfDay;
    private boolean[] daysOfWeek;

    public Song(){
        favorited = false;
        disliked = false;
        locations = new ArrayList<LatLng>();
        timesOfDay = new boolean[3];
        daysOfWeek = new boolean[7];
    }

    /* TODO
     * Make more constructors, including a constructor called by SimpleSongImporter
     */

    public FileDescriptor getFilePath() {
        return filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public ArrayList<LatLng> getLocations() {
        return locations;
    }

    public boolean[] getTimesOfDay() {
        return timesOfDay;
    }

    public boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public Date getLastPlayedTime() {
        return lastPlayedTime;
    }

    public void setLastPlayedTime(Date lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }
}
