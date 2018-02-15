package com.android.flashbackmusic;

import android.app.Application;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Reads song data and metadata from simple local files
 */

public class SimpleSongImporter implements SongImporter {

    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private Application app;

    public SimpleSongImporter(Application app){
        this.app = app;
        songs = new ArrayList<>();
        albums = new ArrayList<>();
    }

    //TODO
    /**
     * Populate list of songs and albums
     * Try using MediaMetadataRetriever class
     */
    public void read(){

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        Field[] fields = R.raw.class.getFields();
        Log.v("LOOK", Integer.toString(fields.length));

        for (Field field : fields) {
            String filePath = "android.resource://com.android.flashbackmusic/raw/" + field.getName();
            mmr.setDataSource(app, Uri.parse(filePath));


            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String track_number = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
            String genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            String year =  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
            String album_name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String album_art= mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);

            int id = app.getResources().getIdentifier(field.getName(), "raw", app.getPackageName());

            // Log.v("LOOK", title + " | " + artist + " | " + track_number + " | " + genre + " | " + year + " | " + album_name);

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

            Song newSong = new Song(id, title, artist, album, album_art, track_number, genre, year);

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