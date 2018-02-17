package com.android.flashbackmusic;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Plays a specific song, and is responsible for displaying metadata as well
 */

public class Player {

    private MediaPlayer mediaPlayer;
    private Application app;

    public Player(Application app){
        this.app = app;
        loadMedia();
    }

    public void play(Song s){

        mediaPlayer.reset();
        loadMedia();

        Log.v("LOOK", s.getTitle() + " should be played right now, id: " + s.getId());

        try {
            AssetFileDescriptor afd = app.getResources().openRawResourceFd(s.getId());
            mediaPlayer.setDataSource(afd);
            mediaPlayer.prepareAsync();
        } catch(Exception e) {
            Log.v("LOOK", e.toString());
        }
    }

    public void togglePausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    private void loadMedia() {
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
}
