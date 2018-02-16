package com.android.flashbackmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentSongBlock extends LinearLayout {

    String titleText;
    String artistName;
    String albumName;
    Boolean favorite;
    Boolean dislike;

    TextView title;
    TextView artist;
    TextView album;
    ImageButton playPause;
    ImageButton favoriteBtn;
    TextView history;

    public CurrentSongBlock(Context context) {
        super(context);
        initializeViews(context);
    }

    public CurrentSongBlock(Context context, Song song) {
        super(context);

        this.titleText = song.getTitle();
        this.artistName = song.getArtist();
        this.albumName = song.getAlbum().getTitle();
        this.favorite = song.isFavorited();
        this.dislike = song.isDisliked();

        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.current_song_block, this);
    }

    public void setText() {
        title = this.findViewById(R.id.current_song_title);
        title.setText(titleText);

        artist = this.findViewById(R.id.current_song_artist);
        artist.setText(artistName);

        album = this.findViewById(R.id.current_song_album);
        album.setText(albumName);
    }

    public void setHistory(String s) {
        history = this.findViewById(R.id.song_history_description);
        history.setText(s);
    }


    public void setFavorite(String fav) {
        favoriteBtn = this.findViewById(R.id.current_song_favorite);

        /*
        if (fav.equals("neutral")) {
            favoriteBtn.setImageResource(R.drawable.);
        } else if (fav.equals("favorite")) {
            favoriteBtn.setImageResource(R.drawable.);
        } else {
            favoriteBtn.setImageResource();
        }
        */
    }

}
