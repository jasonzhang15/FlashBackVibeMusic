package com.android.flashbackmusic;
import com.android.flashbackmusic.CurrentSongBlock;
import com.android.flashbackmusic.Player;
import com.android.flashbackmusic.R;
import com.android.flashbackmusic.SimpleSongImporter;
import com.android.flashbackmusic.SongBlock;

        import android.app.Application;
        import android.app.FragmentTransaction;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import java.util.ArrayList;

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
    private Application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        player = new Player(app);

        CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
        csb.setPlayPause(player);

        loadSongs();
    }

    public void loadSongs() {
        final ArrayList<Song> songList = songImporter.getSongList();
        final LinearLayout layout = findViewById(R.id.main_layout);

        for (Song song : songList) {
            final Song songToPlay = song;

            SongBlock songBlock = new SongBlock(getApplicationContext(), song);
            songBlock.setText();
            songBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CurrentSongBlock csb = findViewById(R.id.current_song_block_main);
                    csb.display();
                    csb.setText(songToPlay);
//                    csb.setHistory("You're listening from " + songToPlay.getLocations() + " on a "
//                            + songToPlay.getDaysOfWeek() + " " + songToPlay.getTimesOfDay());
                    csb.setHistory("You're listening from " + "San Diego" + " on a "
                            + "Tuesday" + " " + "Morning");
                    player.play(songToPlay);
                    csb.togglePlayPause();
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
        }
