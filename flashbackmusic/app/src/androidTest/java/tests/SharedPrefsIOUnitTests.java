package tests;

import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;

import com.android.flashbackmusic.Album;
import com.android.flashbackmusic.MainActivity;
import com.android.flashbackmusic.SharedPrefsIO;
import com.android.flashbackmusic.Song;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by joswei on 2/18/18.
 */

public class SharedPrefsIOUnitTests {

    private SharedPreferences prefs;
    private SharedPrefsIO prefsIO;
    private Song song;
    private Set<String> set;
    private LatLng test_loc;
    private ArrayList<LatLng> locs;
    private Date testTime;
    private Set<String> timesOfDay;
    private Set<String> daysOfWeek;
    private Album album;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() {
        prefs = mainActivity.getActivity().getSharedPreferences("test", mainActivity.getActivity().MODE_PRIVATE);
        prefsIO = new SharedPrefsIO(prefs);

        album = new Album("Album_1");
        song = new Song("Title_1", "Artist_1", album, "url", "path");
        //song = new Song(0, "Title_1", "Artist_1", album, "Album_art_1", "0", "Genre_1", "2018");

        song.setDisliked(true);
        song.setFavorited(false);
        testTime = new Date(1000000);
        song.setLastPlayedTime(testTime);
        locs = song.getLocations();
        test_loc = new LatLng(10, 45);
        locs.add(test_loc);
        song.setLocations(locs);
        set = new HashSet<>();
        set.add("(10.0,45.0)");
        song.setLastLocation(test_loc);
        timesOfDay = song.getTimesOfDay();
        timesOfDay.add("MORNING");
        timesOfDay.add("AFTERNOON");
        daysOfWeek = song.getDaysOfWeek();
        daysOfWeek.add("MONDAY");

        SharedPreferences.Editor editor = prefs.edit();
        String keyPrefix = song.getTitle() + song.getArtist();
        editor.putBoolean(keyPrefix + "disliked", true);
        editor.putBoolean(keyPrefix + "favorited", false);
        editor.putStringSet(keyPrefix + "locs", set);
        editor.putStringSet(keyPrefix + "daysofweek", daysOfWeek);
        editor.putStringSet(keyPrefix + "timesofday", timesOfDay);
        editor.putString(keyPrefix + "lastloc", test_loc.toString());

    }
//public Song(int id, String title, String artist, Album album, String album_art, String track_number, String genre, String year) {

    @Test
    public void testpopulateSongInfo() {
        Song s = new Song(0, "Title_1", "Artist_1", album, "Album_art_1", "0", "Genre_1", "2018");

        prefsIO.populateSongInfo(s);

        assertTrue(s.isDisliked());
        assertFalse(s.isFavorited());
        assertEquals(s.getLocations(), locs);
        assertEquals(s.getTimesOfDay(), timesOfDay);
        assertEquals(s.getDaysOfWeek(), daysOfWeek);
        assertEquals(s.getLastLocation(), test_loc);
    }

    @Test
    public void teststoreSongInfo() {
        prefsIO.storeSongInfo(song);

        String keyPrefix = song.getTitle() + song.getArtist();

        assertTrue(prefs.getBoolean(keyPrefix + "disliked", false));
        assertFalse(prefs.getBoolean(keyPrefix + "favorited", false));
        //assertEquals(prefs.getStringSet(keyPrefix + "locs", null), set);
        assertEquals(prefs.getStringSet(keyPrefix + "timesofday", null), timesOfDay);
        assertEquals(prefs.getStringSet(keyPrefix + "daysofweek", null), daysOfWeek);
        //assertEquals(prefs.getString(keyPrefix + "lastloc", ""), "(10.0,45.0)");
    }
    @Before
    public void teardown() {

    }
}
