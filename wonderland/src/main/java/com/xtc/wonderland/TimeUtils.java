package com.xtc.wonderland;

import java.util.Calendar;

public class TimeUtils {

    public static float getHourPercent(Calendar calendar) {
        float seconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600f
                + calendar.get(Calendar.MINUTE) * 60f
                + calendar.get(Calendar.SECOND);
        return seconds / 43200.0f;
    }

    public static float getMinutePercent(Calendar calendar) {
        float seconds = calendar.get(Calendar.SECOND)
                + calendar.get(Calendar.MINUTE) * 60f;
        return seconds / 3600.0f;
    }

    public static float getSecendPercent(Calendar calendar) {
        return calendar.get(Calendar.SECOND) / 60.0f;
    }
}
