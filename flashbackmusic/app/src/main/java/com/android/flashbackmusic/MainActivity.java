package com.android.flashbackmusic;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private Player player;
    private SimpleSongImporter songImporter;
    private SharedPreferences prefs;
    private SharedPrefsIO prefsIO;
    private Application app;
    private ArrayList<Song> songList;
    private ArrayList<Album> albumList;
    private CurrentParameters currentParameters;
    private LocationAdapter locationAdapter;

    private SongMode sm;
    private AlbumMode am;
    private FlashbackMode fm;
    private CurrentSongBlock csb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        songImporter.read();
        prefs = getSharedPreferences("info", MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        songList = songImporter.getSongList();
        albumList = songImporter.getAlbumList();
        populateSongInfo();
        player = new Player(app);

        // Create the adapter to handle location tracking
        locationAdapter = new LocationAdapter();
        locationAdapter.establishLocationPermission(this, this);
        //locationAdapter.getCurrentLocation();

        currentParameters = new CurrentParameters(locationAdapter);

        am = findViewById(R.id.album_main);
        sm = findViewById(R.id.song_main);
        fm = findViewById(R.id.flashback_main);
        csb = findViewById(R.id.current_song_block_main);

        loadSongs();
        loadAlbums();

        SwitchActivity swc = findViewById(R.id.switch_between_main);

        Button songsTab = swc.getSongs();
        songsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.display(false);
                am.display(false);
                sm.display(true);
                //setContentView(R.layout.song_mode);


                // if music is playing, show csb
            }
        });

        Button album = swc.getAlbum();
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.display(false);
                fm.display(false);
                Log.v("album button pressed", "album");
                //csb.display(false);
                am.display(true);
                //setContentView(R.layout.album_mode);

            }
        });

        Button flashback = swc.getFlashback();
        flashback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.display(false);
                am.display(false);
                fm.display(true);
                //setContentView(R.layout.flashback_mode);


                // disable songs and album tabs?

                loadFlashback();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationAdapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPause() {
        super.onPause();
        storeSongInfo();
        /*CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        SwitchActivity swc = findViewById(R.id.switch_between_main);
        swc.display();
        loadSongs(csb);
        Button album = swc.getAlbum();
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAlbum();
            }
        });*/
    }

    private void loadAlbums() {
        for (Album album : albumList) {
            final AlbumBlock albumBlock = new AlbumBlock(getApplicationContext(), album);
            final Album albumtoPlay = album;
            albumBlock.setText();
            Log.v("albums", "in loadAlbums " + album.getTitle());
            albumBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    albumBlock.setPlayPause(player);
                    albumtoPlay.play(player);
                }
            });
            am.addView(albumBlock);

        }
    }

    public void loadFlashback() {
        FlashbackOrderGenerator fog = new FlashbackOrderGenerator(new CurrentParameters(locationAdapter), songList);
        List<Song> flashbackSongs= fog.getSongList();

        csb.display(false);

        player.reset();
        fog.play(player);

        fm.setPlayPause(player);
        fm.setText(flashbackSongs.get(0));
        fm.setHistory("You're listening from " + "San Diego" + " on a "
                + "Tuesday" + " " + "Morning");

        Button disableFlashback = findViewById(R.id.flashback_disable);
        disableFlashback.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                player.reset();
                fm.display(false);
                sm.display(true);
            }
        });
    }

    public void loadSongs() {
        for (Song song : songList) {
            final Song songToPlay = song;

            final SongBlock songBlock = new SongBlock(getApplicationContext(), song);
            songBlock.setText();
            songBlock.loadFavor(song, prefsIO);
            songBlock.setTime();
            songBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(songToPlay.isDisliked())) {

                        csb.display(true);
                        csb.setText(songToPlay);
                        csb.setPlayPause(player);

                        // TODO: Figure out why this gets a nullreferenceexception
                        // why is locationAdapter null?
                        //LatLng loc = currentParameters.getLocation();
                        String place = "San Diego";
                        String timeOfDay = currentParameters.getTimeOfDay();
                        Date lastPlayedTime = currentParameters.getLastPlayedTime();
                        String day = currentParameters.getDayOfWeek();
                        csb.setHistory("You're listening from " + place + " on a "
                                + day + " " + timeOfDay);
                        player.play(songToPlay);

                        // TODO: once the null pointer reference is fixed, uncomment this line too
                        //songToPlay.setLastLocation(loc);
                        Set<String> timesOfDay = songToPlay.getTimesOfDay();
                        timesOfDay.add(timeOfDay);
                        songToPlay.setTimesOfDay(timesOfDay);
                        songToPlay.setLastPlayedTime(lastPlayedTime);
                        csb.loadFavor(songToPlay, prefsIO, songBlock);
                        csb.setText(songToPlay);
                        csb.togglePlayPause();
                    }
                }
            });
            sm.addView(songBlock);
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

