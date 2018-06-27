package com.example.simple.simpleandroidpractice.videoPlay;

public class WatchVideoTimeUtil {

    public static String formatTime(int timeMilli) {
        int time = timeMilli / 1000;
        int hourTemp = time / (60 * 60);
        StringBuilder sb = new StringBuilder();
        String hour = String.format("%02d", hourTemp);
        sb.append(hour);
        sb.append(":");
        int minuteTemp = time - hourTemp * 60 * 60;
        minuteTemp = minuteTemp / 60;
        String minute = String.format("%02d", minuteTemp);
        sb.append(minute);
        sb.append(":");
        int secodeTemp = time - hourTemp * 60 * 60 - minuteTemp * 60;
        String secode = String.format("%02d", secodeTemp);
        sb.append(secode);
        return sb.toString();
    }
}
