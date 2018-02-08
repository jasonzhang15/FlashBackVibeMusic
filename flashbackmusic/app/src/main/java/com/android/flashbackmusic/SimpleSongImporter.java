package com.android.flashbackmusic;

import java.io.File;
import java.util.ArrayList;

/**
 * Reads song data and metadata from simple local files
 */

public class SimpleSongImporter implements SongImporter {

    private File mainFolder;
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;

    public SimpleSongImporter(String mainFolderLocation){
        mainFolder = new File(mainFolderLocation);
        songs = new ArrayList<Song>();
        albums = new ArrayList<Album>();
    }

    //TODO
    /**
     * Populate list of songs and albums
     * Try using MediaMetadataRetriever class
     */
    public void read(){

    }

    public ArrayList<Song> getSongList() {
        return songs;
    }

    public ArrayList<Album> getAlbumList() {
        return albums;
    }

}
