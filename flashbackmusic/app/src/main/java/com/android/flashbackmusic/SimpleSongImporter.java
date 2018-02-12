package com.android.flashbackmusic;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.ArrayList;

/**
 * Reads song data and metadata from simple local files
 */

public class SimpleSongImporter implements SongImporter {

    // private File mainFolder;
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;

    public SimpleSongImporter(/*String mainFolderLocation*/){
        // mainFolder = new File(mainFolderLocation);
        songs = new ArrayList<Song>();
        albums = new ArrayList<Album>();
    }

    //TODO
    /**
     * Populate list of songs and albums
     * Try using MediaMetadataRetriever class
     */
    public void read(){

        // for each song in res/raw

        System.out.println("Enter read");
        File dir = new File("../../res/raw");
        System.out.println("before song list");
        File[] songPaths = dir.listFiles();

        System.out.println("songs listed");

        if (songPaths == null) {
            System.out.println("Donwnload some songs! You have none right now.");
            return;
        }

        for (File songPath : songPaths) {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//            StringBuilder sb = new StringBuilder();
//
//            String[] vals = songPath.getAbsolutePath().split("-");
//            for (String val : vals) {
//                sb.append(val);
//            }

//            System.out.println("REACHED: " + sb.toString());

            mmr.setDataSource(songPath.getAbsolutePath());

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String track_number = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
            String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            String year =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            // String comments =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_)

            String album_name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String album_art= mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);


            Album album = null;

            for ( Album al : albums ) {
                if (al.getTitle().equals(album_name) ) {
                    album = al;
                    break;
                }
            }

            if (album == null) {
                album = new Album(album_name);
                albums.add(album);
            }

            Song newSong = new Song(title, artist, album, album_art, track_number, genre, year);

            album.addSong(newSong);
            songs.add(newSong);

        }
    }

    public ArrayList<Song> getSongList() {
        return songs;
    }

    public ArrayList<Album> getAlbumList() {
        return albums;
    }

}
