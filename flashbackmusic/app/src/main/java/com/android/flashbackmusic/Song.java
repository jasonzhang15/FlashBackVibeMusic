package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores information about a single song
 */

public class Song {

    private String title;
    private String artist;
    private Album album;
    private String URL;
    private ArrayList<SongPlay> plays;

    private boolean favorited;
    private boolean disliked;
    private ArrayList<LatLng> locations;
    private Date lastPlayedTime;
    private Set<String> timesOfDay;
    private Set<String> daysOfWeek;
    private LatLng lastLocation;

    private int id;

    public Song(){
        favorited = false;
        disliked = false;
        locations = new ArrayList<>();
        timesOfDay = new HashSet<>();
        daysOfWeek = new HashSet<>();
        lastPlayedTime = null;
        lastLocation = new LatLng(0,0);
    }

    public Song(int id, String title, String artist, Album album) {
        this();
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album= album;
        this.lastPlayedTime = new Date(100000000);
        plays = new ArrayList<>();
    }

    // Song Info

    public int getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public Album getAlbum() {
        return album;
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

    public Date getLastPlayedTime() {
        return lastPlayedTime;
    }
    public void setLastPlayedTime(Date lastPlayedTime) {
        this.lastPlayedTime = lastPlayedTime;
    }

    public LatLng getLastLocation() { return lastLocation; }
    public void setLastLocation(LatLng lastLocation) { this.lastLocation = lastLocation; }

    public String getURL() { return URL; }
    public void setURL(String url) { URL = url; }

    public ArrayList<SongPlay> getPlays() { return plays; }
    public SongPlay getMostRecentPlay() { return plays.get(plays.size() - 1); }
    public void addPlay(SongPlay newPlay) { plays.add(newPlay); }
}
