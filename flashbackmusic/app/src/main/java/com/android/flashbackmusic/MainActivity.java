package com.android.flashbackmusic;

import android.app.Application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;

import java.util.ArrayList;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in meory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Player player;
    private SimpleSongImporter songImporter;
    private SharedPreferences prefs;
    private SharedPrefsIO prefsIO;
    private Application app;

    private ArrayList<Song> songList;

    private LocationAdapter locationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        // mViewPager = findViewById(R.id.container);
        // mViewPager.setAdapter(mSectionsPagerAdapter);
        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        songImporter.read();
        prefs = getSharedPreferences("info", MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        songList = songImporter.getSongList();
        populateSongInfo();
        player = new Player(app);
        Log.v("LOOK", Integer.toString(songImporter.getAlbumList().size()));
        Log.v("LOOK", Integer.toString(songImporter.getSongList().size()));

        // Create the adapter to handle location tracking
        locationAdapter = new LocationAdapter(); //LocationServices.getFusedLocationProviderClient(this));
        locationAdapter.establishLocationPermission(this, this);
        //locationAdapter.getCurrentLocation();
        CurrentParameters currentParameters = new CurrentParameters(locationAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        csb.setPlayPause(player);


        loadSongs(csb);
    }

    @Override
    public void onPause() {
        super.onPause();

        storeSongInfo();
        SwitchActivity swc = findViewById(R.id.switch_between_main);
        swc.display();
        loadSongs();


        Button album = swc.getAlbum();
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbum();
            }
        });

    }

    public void launchAlbum() {
        Intent intent = new Intent(this, Album_Activity.class);
        startActivity(intent);
    }

    public void loadSongs(CurrentSongBlock csb) {
        final LinearLayout layout = findViewById(R.id.main_layout);
        for (Song song : songList) {
            final Song songToPlay = song;

            final SongBlock songBlock = new SongBlock(getApplicationContext(), song);
            songBlock.setText();
            songBlock.loadFavor(song, prefsIO);
            songBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(songToPlay.isDisliked())) {

                        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
                        csb.display();
                        csb.loadFavor(songToPlay, prefsIO, songBlock);
                        csb.setText(songToPlay);
                        //                    csb.setHistory("You're listening from " + songToPlay.getLocations() + " on a "
                        //                            + songToPlay.getDaysOfWeek() + " " + songToPlay.getTimesOfDay());
                        csb.setHistory("You're listening from " + "San Diego" + " on a "
                                + "Tuesday" + " " + "Morning");
                        player.play(songToPlay);
                        Date c = Calendar.getInstance().getTime();
                        songToPlay.setLastPlayedTime(c);
                        csb.togglePlayPause();
                    }
                }
            });
            layout.addView(songBlock);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        return true;
        }

        return super.onOptionsItemSelected(item);
        }

    private void populateSongInfo() {
        for (Song song : songList) {
            Log.v("jocelyn", song.getTitle());
            prefsIO.populateSongInfo(song);
        }
    }

    private void storeSongInfo() {
        for (Song song : songList) {
            Log.v("jocelyn", song.getTitle());
            prefsIO.storeSongInfo(song);
        }
    }

}
