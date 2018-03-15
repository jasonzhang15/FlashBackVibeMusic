package com.android.flashbackmusic;

import java.util.Calendar;
import java.util.Date;
/**
 * Created by raindrop on 3/14/18.
 */

public class Time {

    private boolean mocking = false;
    private Date mockTime;

    public Time() {
        this.mocking = false;
    }

    public Time(boolean mockTime, Date theMockTime) {
        this.mockTime = theMockTime;
        this.mocking = mockTime;
    }

    public Date getCurrentTime() {
        if (mocking) {
            return mockTime;
        } else {
            return Calendar.getInstance().getTime();
        }
    }

    public void setCurrentTime(Date time) {
        this.mockTime = time;
    }

}
