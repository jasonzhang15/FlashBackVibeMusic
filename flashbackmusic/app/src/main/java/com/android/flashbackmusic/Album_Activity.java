package com.android.flashbackmusic;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Album_Activity extends AppCompatActivity {
    private ViewPager mViewPager;
    private Player player;
    private SimpleSongImporter songImporter;
    private Application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        songImporter.read();

        player = new Player(app);

        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        csb.setPlayPause(player);

        SwitchActivity swc = findViewById(R.id.switch_between_main);
        swc.display();

        loadAlbums();

        Button getBackSongs = (Button) findViewById(R.id.Songs);

        getBackSongs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                finish();
            }
        });
    }

    public void loadAlbums() {
        final ArrayList<Song> songList = songImporter.getSongList();
        final ArrayList<Album> albumList = songImporter.getAlbumList();
        final LinearLayout layout = findViewById(R.id.main_layout);


        for (Album album : albumList) {
            final AlbumBlock albumBlock = new AlbumBlock(getApplicationContext(), album);
            final Album albumtoPlay = album;
            albumBlock.setText();
            albumBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    albumBlock.setPlayPause(player);
                    albumtoPlay.play(player);
                    albumBlock.togglePlayPause();
                }
            });
            layout.addView(albumBlock);
        }

    }

}
