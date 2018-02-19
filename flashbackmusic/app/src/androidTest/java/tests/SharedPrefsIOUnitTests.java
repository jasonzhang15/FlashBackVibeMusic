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
import java.util.Set;


/**
 * Created by joswei on 2/18/18.
 */

public class SharedPrefsIOUnitTests {

    private SharedPreferences prefs;

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() {
        prefs = mainActivity.getActivity().getSharedPreferences("test", mainActivity.getActivity().MODE_PRIVATE);
    }
//public Song(int id, String title, String artist, Album album, String album_art, String track_number, String genre, String year) {

    @Test
    public void testpopulateSongInfo() {
        Album album = new Album("Album_1");
        Song song = new Song(0, "Title_1", "Artist_1", album, "Album_art_1", "0", "Genre_1", "2018");

        SharedPrefsIO prefsIO = new SharedPrefsIO(prefs);

        song.setDisliked(true);
        song.setFavorited(false);
        Date testTime = new Date(1000000);
        song.setLastPlayedTime(testTime);
        ArrayList<LatLng> locs = song.getLocations();
        LatLng test_loc = new LatLng(10, 45);
        locs.add(test_loc);
        song.setLocations(locs);
        Set<String> timesOfDay = song.getTimesOfDay();
        timesOfDay.add("MORNING");
        timesOfDay.add("AFTERNOON");
        Set<String> daysOfWeek = song.getDaysOfWeek();
        daysOfWeek.add("MONDAY");

        prefsIO.populateSongInfo(song);
    }

    @Test
    public void teststoreSongInfo() {

    }
    @Before
    public void teardown() {

    }
}
