package com.android.flashbackmusic;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nataliepopescu on 2/17/18.
 */

public class CurrentParameters {
    private LocationInterface locationHandler;
    private LatLng location;
    private String dayOfWeek;
    private String timeOfDay;
    private Date lastPlayedTime;

    CurrentParameters(LocationInterface loc) {
        // Location
        locationHandler = loc;
        location = new LatLng(loc.getLatitude(), loc.getLongitude());

        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar calendar = Calendar.getInstance(tz);
        lastPlayedTime = calendar.getTime();

        // Day of Week
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

        // Time of Day
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 11) timeOfDay = "MORNING";
        else if (hour >= 11 && hour < 17) timeOfDay = "AFTERNOON";
        else timeOfDay = "NIGHT";

    }

    public LatLng getLocation() { return location; }

    public String getDayOfWeek() { return dayOfWeek; }

    public String getTimeOfDay() { return timeOfDay; }

    public Date getLastPlayedTime() { return lastPlayedTime; }

    public void setLocation(LocationInterface loc) { location = new LatLng(loc.getLatitude(), loc.getLongitude()); }

    public void setDayOfWeek(String dow) { dayOfWeek = dow; }

    public void setTimeOfDay(String tod) { timeOfDay = tod; }
}
