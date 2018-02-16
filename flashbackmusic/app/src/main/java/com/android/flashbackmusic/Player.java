package com.android.flashbackmusic;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Plays a specific song, and is responsible for displaying metadata as well
 */

public class Player {

    private MediaPlayer mediaPlayer;
    private Application app;
    private List<SongCompletionListener> songCompletionListenerList = new ArrayList<SongCompletionListener>();

    //TODO: Setup media player
    public Player(Application app){
        this.app = app;
        loadMedia();
    }

    //TODO: Play song, and call display song
    //Possibly act as event initiator and provide callback
    public void play(Song s){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            loadMedia();
        }

        Log.v("LOOK", s.getTitle() + " should be played right now, id: " + s.getId());

        try {
            AssetFileDescriptor afd = app.getResources().openRawResourceFd(s.getId());
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepareAsync();
        } catch(Exception e) {
            Log.v("LOOK", e.toString());
        }
    }

    //TODO: Get data from song and display within player area
    public void display(Song s){
        String title = s.getTitle();
        String artist = s.getArtist();
        String album = s.getAlbum().getTitle();

    }

    public void loadMedia() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    public void addSongCompletionListener(SongCompletionListener s){
        this.songCompletionListenerList.add(s);
    }

    public void songCompletionEvent(){
        for (SongCompletionListener s : songCompletionListenerList){
            s.onSongCompletion();
        }
    }

    public boolean isPlaying(){
        return this.mediaPlayer.isPlaying();
    }
}
