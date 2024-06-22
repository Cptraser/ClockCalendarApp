package org.example.Item;

import java.util.Timer;
import java.util.TimerTask;

public class HabitItem {
    private String startTime;
    private String frequency;
    private String time;
    private String content;
    private Timer timer; // 定时器对象

    public HabitItem(String startTime,String frequency, String time, String content, Timer timer) {
        this.startTime = startTime;
        this.frequency = frequency;
        this.time = time;
        this.content = content;
        this.timer = timer;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
