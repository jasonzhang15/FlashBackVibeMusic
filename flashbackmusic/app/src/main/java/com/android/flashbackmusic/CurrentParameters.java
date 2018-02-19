package com.android.flashbackmusic;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nataliepopescu on 2/17/18.
 */

public class CurrentParameters {
    private LocationInterface locationHandler;
    private Calendar calendar;
    private LatLng location;
    private String dayOfWeek;
    private String timeOfDay;
    private Date lastPlayedTime;
  
    private TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");

    public CurrentParameters() {}

    public CurrentParameters(LocationInterface loc) {
        // Location
        locationHandler = loc;
        location = getLocation();
        Log.d("cur location", "" + location);

        calendar = Calendar.getInstance(tz);

        dayOfWeek = getDayOfWeek();

        timeOfDay = getTimeOfDay();
      
        lastPlayedTime = calendar.getTime();
    }

    public LatLng getLocation() { return locationHandler.getCurrentLocation(); }

    public String getDayOfWeek() {
        calendar = Calendar.getInstance(tz);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1: dayOfWeek = "MONDAY";
            case 2: dayOfWeek = "TUESDAY";
            case 3: dayOfWeek = "WEDNESDAY";
            case 4: dayOfWeek = "THURSDAY";
            case 5: dayOfWeek = "FRIDAY";
            case 6: dayOfWeek = "SATURDAY";
            case 7: dayOfWeek = "SUNDAY";
        }
        return dayOfWeek;
    }

    public String getTimeOfDay() {
        calendar = Calendar.getInstance(tz);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 11) timeOfDay = "MORNING";
        else if (hour >= 11 && hour < 17) timeOfDay = "AFTERNOON";
        else timeOfDay = "NIGHT";
        return timeOfDay;
    }

    public Date getLastPlayed() {
        calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public Date getLastPlayedTime() { return lastPlayedTime; }

    public void setLocation(LocationInterface loc) { location = new LatLng(loc.getLatitude(), loc.getLongitude()); }

    //For testing purposes
    protected void setLatLng(LatLng latLng){location = latLng;}

    public void setDayOfWeek(String dow) { dayOfWeek = dow; }

    public void setTimeOfDay(String tod) { timeOfDay = tod; }
}
