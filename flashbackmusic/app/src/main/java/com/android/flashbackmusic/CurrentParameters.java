package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

import java.time.DayOfWeek;

/**
 * Created by nataliepopescu on 2/17/18.
 */

public class CurrentParameters {
    LocationInterface locationHandler;
    LatLng location;
    DayOfWeek dayOfWeek;
    int timeOfDay;

    public static final int MORNING = 0;
    public static final int AFTERNOON = 1;
    public static final int NIGHT = 2;

    public CurrentParameters(LocationInterface loc, DayOfWeek dow, int tod) {
        locationHandler = loc;
        location = new LatLng(loc.getLatitude(), loc.getLongitude());
        dayOfWeek = dow;
        timeOfDay = tod;
    }

    public LatLng getLocation() { return location; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }

    public int getTimeOfDay() { return timeOfDay; }

    public void setLocation(LocationInterface loc) { location = new LatLng(loc.getLatitude(), loc.getLongitude()); }

    public void setDayOfWeek(DayOfWeek dow) { dayOfWeek = dow; }

    public void setTimeOfDay(int tod) { timeOfDay = tod; }
}
