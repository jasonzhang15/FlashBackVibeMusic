package com.android.flashbackmusic;

import android.app.Application;
import android.app.DownloadManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Reads song data and metadata from simple local files
 */

public class SimpleSongImporter implements SongImporter {

    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private Application app;
    private DownloadManager downloadManager;

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
        Log.v("LOOK", Integer.toString(fields.length));

        for (Field field : fields) {
            String filePath = "android.resource://com.android.flashbackmusic/raw/" + field.getName();
            mmr.setDataSource(app, Uri.parse(filePath));

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            int id = app.getResources().getIdentifier(field.getName(), "raw", app.getPackageName());

            Album album = addAlbum(albumName);

            addSong(id, title, artist, album);
        }
    }

    public long downloadSong(String url) {

        Uri uri = Uri.parse(url);

        // Create request for android download manager
        downloadManager = (DownloadManager) app.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalFilesDir(app, Environment.DIRECTORY_DOWNLOADS,"test.mp3");

        //Enqueue download and save into referenceId

        long downloadReference = downloadManager.enqueue(request);



        return downloadReference;
    }

    private void addSong(int id, String title, String artist, Album album) {
        Song newSong = new Song(id, title, artist, album);

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