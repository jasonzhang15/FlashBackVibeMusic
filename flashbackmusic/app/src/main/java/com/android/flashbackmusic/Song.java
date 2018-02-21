package com.android.flashbackmusic;

import java.io.FileDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.android.gms.maps.model.LatLng;

/**
 * Stores information about a single song
 */

class Song {

    private FileDescriptor filePath;
    private String title;
    private String artist;
    private String genre;
    private String year;
    private Album album;
    private String album_art;
    private String track_number;

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
    }

    public Song(int id, String title, String artist, Album album, String album_art, String track_number, String genre, String year) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album= album;
        this.album_art = album_art;
        this.track_number = track_number;
        this.genre = genre;
        this.year = year;
        this.lastPlayedTime = new Date(100000000);

        favorited = false;
        disliked = false;
        locations = new ArrayList<>();
        timesOfDay = new HashSet<>();
        daysOfWeek = new HashSet<>();
    }

    /* TODO
     * Make more constructors, including a constructor called by SimpleSongImporter
     */

    public FileDescriptor getFilePath() {
        return filePath;
    }

    // Song Info

    public int getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {return genre; }

    public String getYear() { return year; }

    public String getAlbum_art() { return album_art; }

    public String getTrack_number() { return track_number; }

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

}
