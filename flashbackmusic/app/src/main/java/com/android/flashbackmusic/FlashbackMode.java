package com.android.flashbackmusic;

/**
 * Created by mac on 20/02/2018.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FlashbackMode extends LinearLayout implements SongView {

    ImageButton playPause;
    ImageButton favoriteBtn;
    Player player;
    Context context;
    Button disableFlashback;

    public FlashbackMode(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.flashback_mode, this);
        //this.hide();
    }

    public void setPlayPause(final Player player) {
        this.player = player;

        playPause = this.findViewById(R.id.flashback_playPause);
        Log.v("zhikai", playPause.toString());

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePlayPause();
                player.togglePausePlay();
            }
        });
    }

    public void togglePlayPause() {
        if (player.isPlaying()) {
            playPause.setImageResource(android.R.drawable.ic_media_play);
        } else {
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    public void hide() {
        this.setVisibility(LinearLayout.INVISIBLE);
    }

    public void display() {
        this.setVisibility(LinearLayout.VISIBLE);
    }

    public void setText(Song song) {
        TextView title = this.findViewById(R.id.flashback_song_title);
        title.setText(song.getTitle());

        TextView artist = this.findViewById(R.id.flashback_song_artist);
        artist.setText(song.getArtist());

        TextView album = this.findViewById(R.id.flashback_song_album);
        album.setText(song.getAlbum().getTitle());
    }

    public void setHistory(String s) {
        TextView history = this.findViewById(R.id.flashback_history_description);
        history.setText(s);
    }
}
