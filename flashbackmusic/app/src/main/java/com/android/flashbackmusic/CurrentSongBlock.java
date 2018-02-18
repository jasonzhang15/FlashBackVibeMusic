package com.android.flashbackmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentSongBlock extends LinearLayout {

    ImageButton playPause;
    ImageButton favoriteBtn;
    Player player;
    Context context;

    public CurrentSongBlock(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.current_song_block, this);
        this.hide();
    }

    public void setPlayPause(final Player player) {
        this.player = player;

        playPause = this.findViewById(R.id.song_playPause);
        Log.v("zhikai", playPause.toString());

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
                player.togglePausePlay();
            }
        });
    }

    public void LoadFavor (SongBlock songBlock){
        favoriteBtn = songBlock.getFavorite();
    }

    public void togglePlayPause() {
        if (player.isPlaying()) {
            playPause.setImageResource(android.R.drawable.ic_media_play);
        } else {
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    public void display() {
        this.setVisibility(LinearLayout.VISIBLE);
    }

    public void hide() {
        this.setVisibility(LinearLayout.INVISIBLE);
    }


    public void setText(Song song) {
        TextView title = this.findViewById(R.id.current_song_title);
        title.setText(song.getTitle());

        TextView artist = this.findViewById(R.id.current_song_artist);
        artist.setText(song.getArtist());

        TextView album = this.findViewById(R.id.current_song_album);
        album.setText(song.getAlbum().getTitle());
    }

    public void setHistory(String s) {
        TextView history = this.findViewById(R.id.song_history_description);
        history.setText(s);
    }

    public ImageButton getFavorite() {
        return this.findViewById(R.id.current_song_favorite);

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
