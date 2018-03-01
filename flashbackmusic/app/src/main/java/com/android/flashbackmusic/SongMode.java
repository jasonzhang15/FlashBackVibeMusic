package com.android.flashbackmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SongMode extends LinearLayout {

    Player player;
    Context context;

    public SongMode(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.song_mode, this);
    }

    public void display(boolean display) {
        if (display)  {
            //this.setVisibility(LinearLayout.VISIBLE);
            display();
        } else {
            //this.setVisibility(LinearLayout.INVISIBLE);
            hide();
        }
    }

    public void hide() {
        this.setVisibility(View.GONE);
    }

    public void display() {
        this.setVisibility(View.VISIBLE);
    }

}
