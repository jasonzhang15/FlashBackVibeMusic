package com.android.flashbackmusic;

public interface SongView {

    void setPlayPause( final Player player);
    void togglePlayPause();
    void display();
    void hide();
    void setText(Song song);
    void setHistory(String s);
}