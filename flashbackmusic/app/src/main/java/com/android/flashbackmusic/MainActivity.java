package com.android.flashbackmusic;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.util.List;

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
    private LocationAdapter locationAdapter;
    private SimpleDownloader downloader;

    private SongMode sm;
    private AlbumMode am;
    private FlashbackMode fm;
    private CurrentSongBlock csb;

    private Context maContext;
    /*
    private GoogleApiClient google_api_client;
    private void buildNewGoogleApiClient() {
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Initiate connection for Google+ API
        google_api_client.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        getProfileInfo();
        changeUI(true);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
            return;
        }
        if (!is_intent_inprogress) {
            connection_result = result;
            if (is_signInBtn_clicked) {
                resolveSignInError();
            }
        }
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
        changeUI(false);
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
            changeUI(false);
        }
    }

    private void gPlusRevokeAcces() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Log.d("MainActivity", "User access revoked!");
                            buildNewGoogleApiClient();
                            google_api_client.connect();
                            changeUI(false);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(this, "start sign process", Toast.LENGTH_SHORT).show();
                gPlusSignIn();
                break;
            case R.id.sign_out_button:
                Toast.makeText(this, "sign out from G+", Toast.LENGTH_LONG).show();
                gPlusSignOut();
                break;
            case R.id.disconnect_button:
                Toast.makeText(this, "Revoke access from G+", Toast.LENGTH_LONG).show();
                gPlusRevokeAcces();
                break;
        }
    }
    private void changeUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    */
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

                    // TODO: Figure out why this gets a nullreferenceexception
                    // why is locationAdapter null?
                    //LatLng loc = currentParameters.getLocation();
                    String place = "San Diego";
                    String timeOfDay = currentParameters.getTimeOfDay();
                    Time lastPlayedTime = currentParameters.getLastPlayedTime();
                    String day = currentParameters.getDayOfWeek();
                    csb.setHistory("You're listening from " + place + " on a "
                            + day + " " + timeOfDay);
                    player.play(songToPlay);

                    // TODO: once the null pointer reference is fixed, uncomment this line too
                    //songToPlay.setLastLocation(loc);
                    songToPlay.addTimeOfDay(timeOfDay);
                    if (!songToPlay.getLastPlayedTime().isMocking()) {
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

