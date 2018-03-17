package com.android.flashbackmusic;

import android.app.Application;
import android.content.Intent;
import android.app.DownloadManager;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements SongCompletionListener, LocationChangeListener, Observer{

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
    private FirebaseIO firebase;

    private SongMode sm;
    private AlbumMode am;
    private FlashbackMode fm;
    private CurrentSongBlock csb;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<SongBlock>> listDataChild;
  
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
        //AsyncTaskRunner runner = new AsyncTaskRunner();
        //runner.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = this.getApplication();
        songImporter = new SimpleSongImporter(app);
        downloader = new SimpleDownloader(app, songImporter);

        final IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloader.downloadReceiver, filter);

        //downloader.downloadSong("http://www.purevolume.com/download.php?id=3463253");
        downloader.downloadSong("http://www.purevolume.com/download.php?id=3061040");
        downloader.downloadSong("https://drive.google.com/a/ucsd.edu/uc?id=1z0hBA6_ZMTokfaJ8qJXHxfbQpedbJi9J&export=download");
        downloader.downloadSong("https://drive.google.com/a/ucsd.edu/uc?id=12NniiNS58swhkA6aYLEsz3PsHh4FOSl1&export=download");
        downloader.downloadSong("https://drive.google.com/a/ucsd.edu/uc?id=1k-O4RHfkjYhVif3Af9tr6mP6x45fkFjY&export=download");

        prefs = getSharedPreferences("info", MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        songImporter.read();
        songList = songImporter.getSongList();
        albumList = songImporter.getAlbumList();
        remoteSongList = songImporter.getRemoteSongList();

        firebase = new FirebaseIO(remoteSongList, songList);

        for (int i = 0; i < songList.size(); i++) {
            Log.v("LOOK", "songs: " + songList.get(i).getTitle());
        }
        populateSongInfo();
        player = new Player(app);

        // Create the adapter to handle location tracking
        locationAdapter = new LocationMock();
        locationAdapter.establishLocationPermission(this, this);
        locationAdapter.addLocationChangeListener(this);
        //locationAdapter.getCurrentLocation();s

        currentParameters = new CurrentParameters(locationAdapter);

        am = findViewById(R.id.album_main);
        sm = findViewById(R.id.song_main);
        fm = findViewById(R.id.flashback_main);
        csb = findViewById(R.id.current_song_block_main);
        //csb2 = findViewById(R.id.current_song_block_mode);

        final Button buttonTitle = findViewById(R.id.buttonTitle);
        final Button buttonArtist = findViewById(R.id.buttonArtist);
        final Button buttonAlbum = findViewById(R.id.buttonAlbum);
        final Button buttonFavorite = findViewById(R.id.buttonFavorite);
        buttonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(songList, new TitleComparator());
                for (Song s :songList) {
                    Log.v("zhikai", s.getTitle());
                }
                loadSongs();
            }
        });
        buttonArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(songList, new ArtistComparator());
                for (Song s :songList) {
                    Log.v("zhikai", s.getTitle());
                    Log.v("zhikai", s.getArtist() == null ? "No artist!!!!" : s.getArtist());
                }
                loadSongs();
            }
        });
        buttonAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(songList, new AlbumComparator());
                for (Song s :songList) {
                    Log.v("zhikai", s.getTitle());
                    Log.v("zhikai", s.getAlbum() == null ? "No album" : s.getAlbum());
                    Log.v("zhikai", "=======");

                }
                loadSongs();
            }
        });
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(songList, new FavoriteComparator());
                for (Song s :songList) {
                    Log.v("zhikai", s.getTitle());
                    Log.v("zhikai", String.valueOf(s.isFavorited()));
                    Log.v("zhikai", "=======");
                }
                loadSongs();
            }
        });
        loadSongs();
        for (Song s :songList) {
            Log.v("init_czk", s.getTitle());
            if (s.getArtist() == null) {
                Log.v("init_czk", "what? No Artist Assigned???");
            } else {
                Log.v("init_czk", s.getArtist());
            }
        }
        //loadAlbums();


        ImageButton refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("LOOK", "REACHED INSIDE OF ONCLICK");
                songImporter = new SimpleSongImporter(app);
                songImporter.read();
                songList = songImporter.getSongList();
                loadSongs();
                loadAlbum();
            }
        });

        final AddMusic addMusic = findViewById(R.id.add_music_main);
        addMusic.getAddMusicButton().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText edit = addMusic.getAddMusicTextEdit();
                String url = edit.getText().toString();
                downloader.downloadSong(url);
                edit.setText("");
                Log.v("LOOK", "REACHED INSIDE OF ADD ONCLICK");
            }
        });

        SwitchActivity swc = findViewById(R.id.switch_between_main);

        Button songsTab = swc.getSongs();
        songsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.display(false);
                am.display(false);
                sm.display(true);
                addMusic.display(true);
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
                addMusic.display(false);
                loadAlbum();
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
                addMusic.display(false);
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
    }


    public void loadAlbum() {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        expListView.setClickable(true);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                final SongBlock selected = (SongBlock) listAdapter.getChild(
                        groupPosition, childPosition);
                final Song songToPlay = selected.getSong();
                selected.setText();
                selected.loadFavor(songToPlay, prefsIO);
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
                Set<String> timesOfDay = songToPlay.getTimesOfDay();
                timesOfDay.add(timeOfDay);
                songToPlay.setTimesOfDay(timesOfDay);
                //songToPlay.setLastPlayedTime(lastPlayedTime);
                csb.loadFavor(songToPlay, prefsIO, selected);
                csb.setText(songToPlay);
                csb.togglePlayPause();
                return true;
            }
        });
    }

    private void prepareListData() {
        final ArrayList<Album> albumList = songImporter.getAlbumList();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<SongBlock>>();
        for (Album album : albumList) {
            listDataHeader.add(album.getTitle()+"|"+album.getArtist());
        }
        
        for(int i = 0; i < albumList.size();i++) {
            List<SongBlock> songblockList = new ArrayList<>();
            Album current = albumList.get(i);
            ArrayList<Song> songlist = current.getSongs();
            for (Song song : songlist) {
                final SongBlock songBlock = new SongBlock(getApplicationContext(), song);
                songblockList.add(songBlock);
            }
            listDataChild.put(listDataHeader.get(i), songblockList); // Header, Child data

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
        for (RemoteSong r : remoteSongList){
            r.album = null;
            r.setSong(null);
        }
        firebase.update();
        Log.v("firebase:", "updating done");
    }

    public void onSongCompletion(){
        updateSong(player.getLastSong());
    }

    public void onLocationChange(LatLng location){
        startIntentService(location);
    }

    //Firebase has been updated
    public void update(Observable o, Object response){
        for (RemoteSong r :remoteSongList){
            if (r.getSong() == null){
                downloader.downloadSong(r.getURL());
            }
        }
    }

    public void loadSongs() {
        for (Song song : songList) {
            addSongBlock(song);
        }
    }

    public void addSongBlock(Song song) {
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
                    LatLng loc = currentParameters.getLocation();
                    String place = "San Diego";
                    String timeOfDay = currentParameters.getTimeOfDay();
                    Time lastPlayedTime = currentParameters.getLastPlayedTime();
                    String day = currentParameters.getDayOfWeek();
                    csb.setHistory("You're listening from " + place + " on a "
                            + day + " " + timeOfDay);
                    player.play(songToPlay);

                    // TODO: once the null pointer reference is fixed, uncomment this line too
                    songToPlay.setLastLocation(loc);
                    //Log.v("LOCATION!!!", loc.toString());
                    Log.v("LOCATION!!!", songToPlay.getLastLocation().toString());
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
    class TitleComparator implements Comparator<Song> {
        @Override
        public int compare(Song s1, Song s2){
            return s1.getTitle().compareTo(s2.getTitle());
        }
    }
    class ArtistComparator implements Comparator<Song> {
        @Override
        public int compare(Song s1, Song s2){
            if (s1.getArtist() == null && s2.getArtist()== null) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
            if (s1.getArtist() == null) {
                return 1;
            }
            if (s2.getArtist() == null) {
                return -1;
            }
            if (s1.getArtist().equals(s2.getArtist())) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
            return s1.getArtist().compareTo(s2.getArtist());
        }
    }

    class AlbumComparator implements Comparator<Song> {
        @Override
        public int compare(Song s1, Song s2) {
            if (s1.getAlbum() == null && s2.getAlbum()== null) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
            if (s1.getAlbum() == null) {
                return 1;
            }
            if (s2.getAlbum() == null) {
                return -1;
            }
            if (s1.getAlbum().equals(s2.getAlbum())) {
                return s1.getTitle().compareTo(s2.getTitle());
            } else {
                return s1.getAlbum().compareTo(s2.getAlbum());
            }
        }
    }

    class FavoriteComparator implements Comparator<Song> {
        @Override
        public int compare(Song s1, Song s2) {
            if (s1.isFavorited() && s2.isFavorited()) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
            if (s1.isFavorited()) {
                return -1;
            }
            if (!s1.isFavorited() && !s2.isFavorited()) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
            if (!s1.isFavorited()) {
                return 1;
            }
            return 0; // this will never run
        }
    }
}

