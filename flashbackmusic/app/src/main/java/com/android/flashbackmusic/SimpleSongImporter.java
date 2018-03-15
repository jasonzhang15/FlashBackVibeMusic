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
    private ArrayList<RemoteSong> remoteSongs;
    private Application app;
    private File downloadDir;

    public SimpleSongImporter(Application app){
        this.app = app;
        songs = new ArrayList<>();
        albums = new ArrayList<>();
        remoteSongs = new ArrayList<>();
        downloadDir = app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * Populate list of songs and albums
     * Try using MediaMetadataRetriever class
     */
    public void  read(){

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        if (downloadDir.exists()) Log.v("LOOK", "DIR EXISTS: " + downloadDir.toString() );
        File[] listAllFiles = downloadDir.listFiles();
        if(listAllFiles == null) Log.v("LOOK", "DIR IS EMPTY");


        String title;
        String artist;
        String albumName;
        Album album;

        //int id = 0;
        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                mmr.setDataSource(currentFile.toString());

                title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);


                Log.v("LOOK", currentFile.getAbsolutePath());
                Log.v("LOOK", currentFile.getName());
                Log.v("LOOK","title: " + title);
                Log.v("LOOK","artist: " + artist );
                Log.v("LOOK","albumName: " + albumName);

                if (title == null || artist == null | albumName == null) continue;

                album = addAlbum(albumName);

                Log.v("LOOK","ADDED");


                addSong(/*id,*/ title, artist, album, "", currentFile.toString());

                //id++;
            }
        }
    }

    private void addSong(/*id,*/ String title, String artist, Album album, String url, String path) {
        Song newSong = new Song(/*id,*/ title, artist, album, url, path);

        album.addSong(newSong);
        remoteSongs.add(newSong.getRemoteSong());
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

    public ArrayList<RemoteSong> getRemoteSongList() {
        return remoteSongs;
    }


}