package com.android.flashbackmusic;

import java.util.ArrayList;

public class RemoteSong {

    private String title;
    private String artist;
    private Album album;
    private String URL;
    private ArrayList<SongPlay> plays;
    private Song song;
    private String id;


    //Every song and remotesong has an ID - it is used to tie them to each other
    public RemoteSong(/*id,*/ String title, String artist, Album album, String url, String id) {
        //this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.URL = url;
        this.id = id;
        plays = new ArrayList<>();
    }

    public String getId() { return id; }
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

    public Song getSong(){ return song; }
    public void setSong(Song s){ this.song = s;}

    public ArrayList<SongPlay> getPlays() { return plays; }
    public SongPlay getMostRecentPlay() { return plays.get(plays.size() - 1); }
    public void addPlay(SongPlay newPlay) { plays.add(newPlay); }

}


