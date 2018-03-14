package com.android.flashbackmusic;

import android.app.Application;
import android.app.DownloadManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

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

    /**
     * Populate list of songs and albums
     * Try using MediaMetadataRetriever class
     */
    public void  read(){

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Field[] fields = R.raw.class.getFields();

        //File downloadDir = app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
/*
        if (downloadDir.exists()) {
            Log.v("LOOK", "FILE EXISTS");

            if(downloadDir.listFiles().length > 0){

                Log.v("LOOK", "DIR IS NOT EMPTY: " + (downloadDir.list().length));

            }
        }

        File[] listAllFiles = downloadDir.listFiles();

        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                //if (currentFile.getName().endsWith("")) {
                    // File absolute path
                    Log.v("LOOK", currentFile.getAbsolutePath());
                    // File Name
                    Log.v("LOOK", currentFile.getName());

                //}
            }
        }
*/
/*
        //File path = app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        String filename = downloadDir + "/" + "3463253" + ".mp3";
        Log.v("LOOK", filename);
        mmr.setDataSource(filename);

        String title1 = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist1 = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String albumName1 = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        int id1 = 20;

        Album album1 = addAlbum(albumName1);

        addSong(id1, title1, artist1, album1, "");
*/
        for (Field field : fields) {

            String filePath = "android.resource://com.android.flashbackmusic/raw/" + field.getName();
            mmr.setDataSource(app, Uri.parse(filePath));

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            int id = app.getResources().getIdentifier(field.getName(), "raw", app.getPackageName());

            Album album = addAlbum(albumName);

            addSong(id, title, artist, album, "");
        }
    }

    private void addSong(int id, String title, String artist, Album album, String url) {
        Song newSong = new Song(id, title, artist, album, url);

        album.addSong(newSong);
        songs.add(newSong);
    }

    private Album addAlbum(String albumName) {
        Album album = null;
        for ( Album al : albums ) {
            if (al.getTitle().equals(albumName) ) {
                album = al;
                return album;
            }
        }

        if (album == null) {
            album = new Album(albumName);
            albums.add(album);
        }

        return album;
    }

    public ArrayList<Song> getSongList() {
        return songs;
    }

    public ArrayList<Album> getAlbumList() {
        return albums;
    }

}