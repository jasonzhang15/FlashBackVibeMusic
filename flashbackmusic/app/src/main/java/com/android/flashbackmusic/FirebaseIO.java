package com.android.flashbackmusic;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vrkumar on 3/1/18.
 */

public class FirebaseIO implements SongInfoIO {
    @Override
    public void setup() {

    }

    public void sync() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myFirebaseRef = database.getReference();

        Query queryRef = myFirebaseRef.orderByChild("studentId").equalTo(idToSearch);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot == null || snapshot.getValue() == null)
                    Toast.makeText(MainActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Faile to read value
                Log.w("TAG1", "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void populateSongInfo(Song s) {

    }

    @Override
    public void storeSongInfo(Song s) {

    }

    @Override
    public void teardown() {

    }
}
