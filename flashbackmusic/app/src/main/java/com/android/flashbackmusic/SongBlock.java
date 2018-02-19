package com.android.flashbackmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongBlock extends LinearLayout {

    private String album_art;
    private String titleText;
    private String artist;
    private String album;
    private int id;

    private TextView title;
    private TextView artistAlbum;
    private ImageButton favorite;
    private Song song;

    public SongBlock(Context context) {
        super(context);
        initializeViews(context);
    }

    public SongBlock(Context context, Song song) {
        super(context);

        this.titleText = song.getTitle();
        this.artist = song.getArtist();
        this.album = song.getAlbum().getTitle();
        this.id = song.getId();
        this.song = song;

        initializeViews(context);
    }

    public void loadFavor(Song song, SharedPrefsIO sp) {
        final Song song_f = song;
        final SharedPrefsIO sp_f = sp;
        favorite = this.findViewById(R.id.song_favorite);
        if (song_f.isFavorited()) {
            favorite.setImageResource(android.R.drawable.checkbox_on_background);
        } else if (song_f.isDisliked()) {
            favorite.setImageResource(android.R.drawable.ic_delete);
        } else{
            favorite.setImageResource(android.R.drawable.ic_input_add);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!song_f.isFavorited() && !song_f.isDisliked()) {
                    favorite.setImageResource(android.R.drawable.checkbox_on_background);
                    //status = 1;
                    song_f.setFavorited(true);

                } else if (song_f.isFavorited()) {
                    favorite.setImageResource(android.R.drawable.ic_delete);
                    //status = -1;
                    song_f.setFavorited(false);
                    song_f.setDisliked(true);
                } else {
                    favorite.setImageResource(android.R.drawable.ic_input_add);
                    //status = 0;
                    song_f.setDisliked(false);
                }

                sp_f.storeSongInfo(song_f);
            }
        });
    }


    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.song_block, this);
    }

    //set the title and artistAlbum
    public void setText() {
        //Log.v("LOOK", "REACHED setText");

        title = this.findViewById(R.id.song_title);
        title.setText(titleText);

        artistAlbum = this.findViewById(R.id.song_artist_album);
        artistAlbum.setText(artist + " | " + album);
    }

    public ImageButton getFavorite() {
        return this.favorite;
    }
}