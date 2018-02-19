package com.android.flashbackmusic;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vrkumar on 2/18/18.
 */

public class FlashbackOrderGenerator {
    final static double MAX_DIST_METERS = 304.8;


    private CurrentParameters c;
    private List<Song> songList;

    public FlashbackOrderGenerator(CurrentParameters c, List<Song> songList){
        this.c = c;
        this.songList = songList;
    }


    public CurrentParameters getC() {
        return c;
    }

    public void setC(CurrentParameters c) {
        this.c = c;
    }

    public List<Song> getSongList() {
        Collections.sort(songList, songComparator);
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    //Note this is in order generator and not in song since different order generators
    //could return different scores for the same song

    public int getScore(Song s){
        if (s.isDisliked()) {
            return -1;
        } else {
            int score = 0;
            if (s.getDaysOfWeek().contains(c.getDayOfWeek())){
                score += 1;
            }
            if (s.getTimesOfDay().contains(c.getTimeOfDay())){
                score += 1;
            }
            LatLng currLoc = c.getLocation();
            float[] results = new float[1];
            for (LatLng l : s.getLocations()){
                Location.distanceBetween(currLoc.latitude, currLoc.longitude, l.latitude, l.longitude, results);
                if (results[0] <= MAX_DIST_METERS){
                    score += 1;
                    break;
                }
            }
            if (s.isFavorited()){
                score += 1;
            }
            return score;
        }
    }

    Comparator<Song> songComparator = new Comparator<Song>(){

        @Override
        public int compare(final Song s1, final Song s2){
            int score1 = getScore(s1);
            int score2 = getScore(s2);

            if (score1 == score2){
                return s1.getLastPlayedTime().after(s2.getLastPlayedTime()) ? 1 : -1;
            } else {
                return score2 - score1;
            }
        }
    };
}
