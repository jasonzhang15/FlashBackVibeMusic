package com.android.flashbackmusic;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by vrkumar on 3/1/18.
 */

public class FirebaseIO extends Observable{

    FirebaseDatabase database;
    DatabaseReference myRef;

    public void setup(final List<Song> songList) {
        database = database.getInstance();
        myRef = database.getReference();

        myRef.setValue(songList);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songList.clear();
                GenericTypeIndicator<List<Song>> t = new GenericTypeIndicator<List<Song>>() {};
                List<Song> newSongs = dataSnapshot.getValue(t);
                songList.addAll(newSongs);
                notifyObservers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Database:", "Database unexpectedly closed");
            }
        });

    }
}
