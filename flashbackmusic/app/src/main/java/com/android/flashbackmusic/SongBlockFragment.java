package com.android.flashbackmusic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SongBlockFragment extends Fragment {

    private Song song;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        View view = inflater.inflate(R.layout.song_block, container, false);

        /*
        TextView title = view.findViewById(R.id.song_title);
        title.setText(song.getTitle());
        // set unique id ???

        TextView artistAlbum = view.findViewById(R.id.song_artist_album);
        String newText = song.getArtist() + " | " + song.getAlbum().getTitle();
        artistAlbum.setText(newText);
        */
        return view;
    }

    public void setSong(Song song) {
        this.song = song;
    }

}
