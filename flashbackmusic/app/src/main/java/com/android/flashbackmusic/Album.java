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

    public void play(final Player p) {

        final Queue<Song> songQueue = new ArrayDeque<Song>(songs);

        if (!p.isPlaying() && !(songQueue.isEmpty())) {
            p.play(songQueue.poll());
        }

        p.addSongCompletionListener(new SongCompletionListener() {
            @Override
            public void onSongCompletion() {
                if (!(songQueue.isEmpty())) {
                    p.play(songQueue.poll());
                }
            }
        });
    }
}
