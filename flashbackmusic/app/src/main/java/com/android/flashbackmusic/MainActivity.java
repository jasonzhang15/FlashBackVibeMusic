package com.android.flashbackmusic;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SongCompletionListener{

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
    private ArrayList<RemoteSong> remoteSongList;
    private CurrentParameters currentParameters;
    //private LocationAdapter locationAdapter;
    private LocationMock locationAdapter;
    private SimpleDownloader downloader;

    private SongMode sm;
    private AlbumMode am;
    private FlashbackMode fm;
    private CurrentSongBlock csb;
    String day = "Sunday";
    String timeOfDay = "night";
    private String place = "San Diego";

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                Log.w("Result data", "was null");
                return;
            }
            // Display the address string
            // or an error message sent from the intent service.
            String address = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.w("RECEIVED RESULT", address);
            Log.w("NEW PLACE", place);
            if (address == null) {
                address = "San Diego";
            }
            place = address;
            csb.setHistory("at " + place + " on a "
                    + day + " " + timeOfDay);
        }
    }
    private AddressResultReceiver mResultReceiver;

    protected void startIntentService(LatLng location) {
        Intent intent = new Intent(MainActivity.this, FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, (Parcelable) mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }
    private Context maContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maContext = this;
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        downloader = new SimpleDownloader(app, songImporter);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloader.downloadReceiver, filter);

        downloader.downloadSong("http://www.purevolume.com/download.php?id=3463253");
        downloader.downloadSong("http://www.purevolume.com/download.php?id=3061040");
        downloader.downloadSong("http://www.purevolume.com/download.php?id=3061067");

        songImporter.read();

        prefs = getSharedPreferences("info", MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        songList = songImporter.getSongList();
        albumList = songImporter.getAlbumList();
        remoteSongList = songImporter.getRemoteSongList();

        for (int i = 0; i < songList.size(); i++) {
            Log.v("LOOK", "songs: " + songList.get(i).getTitle());
        }
        populateSongInfo();
        player = new Player(app);

        // Create the adapter to handle location tracking
        locationAdapter = new LocationMock();
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String ret = "";

        @Override
        protected String doInBackground(String... params) {
            Log.d("in ATR", "test");
            try {
                Intent intent = new Intent(maContext, SignInActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ret = e.getMessage();
            }
            return ret;
        }
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
            + "Sunday" + " " + "night");

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
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.v("addresses", String.valueOf(addresses));
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current location address", strReturnedAddress.toString());
            } else {
                strAdd = "San Diego";
                Log.w("My Current location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location address", "Cannot get Address!");
        }
        return strAdd;
    }

    public void updateSong(Song s){
        String timeOfDay = currentParameters.getTimeOfDay();
        Time lastPlayedTime = currentParameters.getLastPlayedTime();
        s.addTimeOfDay(timeOfDay);
        s.setLastPlayedTime(lastPlayedTime);
        s.addPlay(new SongPlay( "bob", currentParameters.getLocation(), currentParameters.getTimeOfDay(), currentParameters.getLastPlayedTime().getDate()));
    }

    public void onSongCompletion(){
        updateSong(player.getLastSong());
    }

    public void loadSongs() {
        for (final Song song : songList) {
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
                        LatLng loc = currentParameters.getLocation();

                        startIntentService(loc);

                        Time lastPlayedTime = songToPlay.getLastPlayedTime();
                        if (lastPlayedTime == null || !(lastPlayedTime.isMocking())) {
                            Log.v("is lastPlayedTime Null?", String.valueOf(lastPlayedTime == null));
                            currentParameters.setLastPlayedTime(new Time());
                            timeOfDay = currentParameters.getTimeOfDay();
                            lastPlayedTime = currentParameters.getLastPlayedTime();
                            day = currentParameters.getDayOfWeek();
                        } else {
                            Log.v("else", String.valueOf(lastPlayedTime.isMocking()));
                            currentParameters.setLastPlayedTime(lastPlayedTime);
                            timeOfDay = currentParameters.getTimeOfDay();
                            day = currentParameters.getDayOfWeek();
                            Log.v("timeofday, day", String.valueOf(timeOfDay) + " " + String.valueOf(day));
                        }
                        csb.setHistory("at " + place + " on a "
                                + day + " " + timeOfDay);
                        player.play(songToPlay);

                        // TODO: once the null pointer reference is fixed, uncomment this line too
                        songToPlay.setLastLocation(loc);
                        songToPlay.addTimeOfDay(timeOfDay);
                        if (!lastPlayedTime.isMocking()) {
                            songToPlay.setLastPlayedTime(lastPlayedTime);
                        }
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

