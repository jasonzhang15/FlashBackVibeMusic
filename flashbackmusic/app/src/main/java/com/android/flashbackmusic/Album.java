package com.android.flashbackmusic;

/**
 * Stores information about a single album
 */

class Album {

    String title;
    Song[] songs;

    public Album(Song[] songs){
        this.songs = songs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * TODO
     * play(Player p) method
     * That sequentially passes p each song, listens for callback and then passes in the next
     */

}
