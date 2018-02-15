package com.android.flashbackmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by valentinadibs on 2/15/18.
 */

public class SongBlock extends LinearLayout {
    private String titleText;
    private String artist;
    private String album;
    private int id;

    private TextView title;
    private TextView artistAlbum;
    private ImageButton favorite;

    public SongBlock(Context context) {
        super(context);
        initializeViews(context);
    }

    public SongBlock(Context context, String titleText, String artist, String album, int id) {
        super(context);

        Log.v("LOOK", "REACHED song block class: " + titleText);
        this.titleText = titleText;
        this.artist = artist;
        this.album = album;
        this.id = id;
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.song_block, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        title = this.findViewById(R.id.song_title);
        artistAlbum = this.findViewById(R.id.song_artist_album);

        title.setText(titleText);
        artistAlbum.setText(artist + " | " + album);
        /*
        title = this.findViewById(R.id.song_title).setBackgroundResource();
        artistAlbum = this.findViewById(R.id.song_artist_album).setBackgroundResource();
        favorite = this.findViewById(R.id.song_favorite).setBackgroundResource();
        */
    }

}
