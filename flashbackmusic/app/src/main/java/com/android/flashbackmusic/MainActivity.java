package com.android.flashbackmusic;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

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
    private CurrentParameters curr;

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

        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        csb.setPlayPause(player);

        loadSongs();
    }

    @Override
    public void onPause() {
        super.onPause();

        storeSongInfo();
    }

    public void loadSongs() {
        final LinearLayout layout = findViewById(R.id.main_layout);
        for (Song song : songList) {
            final Song songToPlay = song;

            final SongBlock songBlock = new SongBlock(getApplicationContext(), song);
            songBlock.setText();
            songBlock.LoadFavor();
            songBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(songToPlay.isDisliked())) {

                        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
                        csb.display();
                        csb.setText(songToPlay);
                        LatLng loc = curr.getLocation();
                        String place = "San Diego";
                        String timeOfDay = curr.getTimeOfDay();
                        Date lastPlayedTime = curr.getLastPlayedTime();
                        String day = curr.getDayOfWeek();
                        csb.setHistory("You're listening from " + place + " on a "
                                + day + " " + timeOfDay);
                        player.play(songToPlay);
                        songToPlay.setLastLocation(loc);
                        Set<String> timesOfDay = songToPlay.getTimesOfDay();
                        timesOfDay.add(timeOfDay);
                        songToPlay.setTimesOfDay(timesOfDay);
                        songToPlay.setLastPlayedTime(lastPlayedTime);
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

