package com.android.flashbackmusic;

import java.util.ArrayList;
import java.util.List;

public class FlashbackOrderGenerator {
    List<Song> songs;
    int[] songScores;
    User user;

    public FlashbackOrderGenerator(List<Song> songs, User user){
        this.songs = songs;
        this.songScores = new int[songs.size()];
        this.user = user;
    }

    //It is important that this method is in the order generator rather than song
    //It is feasible to foresee different order generators being created later, that order songs
    //in different manners, hence this is not a global song score

    //TODO: Add all other checks
    public int getSongScore(Song s){
        int score = 0;
        if (s.isDisliked()){
            return -1;
        }
        if (s.isFavorited()){
            score += 1;
        }
        return score;
    }

}
