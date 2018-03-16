package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores information about a single song
 */

public class Song {

    private boolean favorited;
    private boolean disliked;
    private ArrayList<LatLng> locations;
    private Time lastPlayedTime;
    private Set<String> timesOfDay;
    private Set<String> daysOfWeek;
    private LatLng lastLocation;
    private boolean local;
    String path;

    private RemoteSong remoteSong;

    public Song(){
        favorited = false;
        disliked = false;
        locations = new ArrayList<>();
        timesOfDay = new HashSet<>();
        daysOfWeek = new HashSet<>();
        lastPlayedTime = null;
        lastLocation = new LatLng(0,0);
        local = false;
    }

    public Song(/*id,*/ String title, String artist, Album album, String url, String path) {
        this();
        this.path = path;
        remoteSong = new RemoteSong(/*id,*/ title, artist, album, url);
    }
    // Song Info

    //public int getId() { return remoteSong.getId(); }
    public String getTitle() {
        return remoteSong.getTitle();
    }
    public String getArtist() {
        return remoteSong.getArtist();
    }
    public Album getAlbum() {
        return remoteSong.getAlbum();
    }

    // User - Song Info

    public ArrayList<LatLng> getLocations() {
        return locations;
    }
    public void setLocations(ArrayList<LatLng> locs) { this.locations = locs; }

    public Set<String> getTimesOfDay() {
        return timesOfDay;
    }
    public void setTimesOfDay(Set<String> times) { this.timesOfDay = times; }
    public void addTimeOfDay(String timeOfDay) { this.timesOfDay.add(timeOfDay); }

    public Set<String> getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(Set<String> days) { this.daysOfWeek = days; }

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

    public Time getLastPlayedTime() {
        return lastPlayedTime;
    }
    public void setLastPlayedTime(Time lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }

    public LatLng getLastLocation() { return lastLocation; }
    public void setLastLocation(LatLng lastLocation) { this.lastLocation = lastLocation; }


    public String getURL() { return remoteSong.getURL(); }
    public void setURL(String url) { remoteSong.setURL(url); }

    public ArrayList<SongPlay> getPlays() { return remoteSong.getPlays(); }
    public SongPlay getMostRecentPlay() { return remoteSong.getMostRecentPlay(); }
    public void addPlay(SongPlay newPlay) {remoteSong.addPlay(newPlay); }

    public boolean getLocal() { return local; }
    public void setLocal(boolean local) { this.local = local; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
