package com.android.flashbackmusic;

import java.util.ArrayList;
import java.util.Date;

public class RemoteSong {

    private String title;
    private String artist;
    private Album album;
    private String URL;
    private ArrayList<SongPlay> plays;
    private int id;

    public RemoteSong(int id, String title, String artist, Album album, String url) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.URL = url;
        plays = new ArrayList<>();
    }

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

    public String getURL() { return URL; }
    public void setURL(String url) { URL = url; }

    public ArrayList<SongPlay> getPlays() { return plays; }
    public SongPlay getMostRecentPlay() { return plays.get(plays.size() - 1); }
    public void addPlay(SongPlay newPlay) { plays.add(newPlay); }

}


