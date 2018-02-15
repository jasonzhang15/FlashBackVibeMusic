package com.android.flashbackmusic;
import java.util.*;
/**
 * Stores information about a single album
 */

class Album {

    String title;
    ArrayList<Song> songs;

    public Album(String title){
        this.title = title;
        songs = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    /*
     * TODO
     * play(Player p) method
     * That sequentially passes p each song, listens for callback and then passes in the next
     */

}
