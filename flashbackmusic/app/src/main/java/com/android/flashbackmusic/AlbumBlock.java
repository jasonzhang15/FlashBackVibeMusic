package com.android.flashbackmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mac on 18/02/2018.
 */

public class AlbumBlock extends LinearLayout {
    private String albumName;
    private String artistName;

    private TextView albumView;
    private TextView artistView;

    private Album album;

    public AlbumBlock(Context context) {
        super(context);
        initializeViews(context);
    }

    public AlbumBlock(Context context, Album album) {
        super(context);
        this.albumName = album.getTitle();
        this.artistName = album.getArtist();
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.album_block, this);
    }

    public void setText() {
        albumView = this.findViewById(R.id.album_title);
        albumView.setText(albumName);
        artistView = this.findViewById(R.id.artist_title);
        artistView.setText(artistName);
    }

    public String getAlbumTitle(){
        return this.albumName;
    }
}
