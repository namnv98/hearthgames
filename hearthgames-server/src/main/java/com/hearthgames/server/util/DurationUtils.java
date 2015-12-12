package com.hearthgames.server.util;

import java.time.Duration;

public class DurationUtils {

    public static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        return String.format("%02d:%02d:%02d", hours, minutes, duration.getSeconds());
    }
}
