package com.android.flashbackmusic;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Album_Activity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Player player;
    private SimpleSongImporter songImporter;
    private Application app;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        songImporter.read();

//        Bundle bundle = new Bundle();
//        player = (Player) bundle.getSerializable("player");

        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("player");

        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        csb.setPlayPause(player);

        SwitchActivity swc = findViewById(R.id.switch_between_main);

        loadAlbums();

        Button getBackSongs = (Button) findViewById(R.id.Songs);

        getBackSongs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                finish();
            }
        });*/
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*public void loadAlbums() {
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

    }*/
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
