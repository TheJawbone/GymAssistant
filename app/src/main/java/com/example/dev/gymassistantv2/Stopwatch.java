package com.example.dev.gymassistantv2;

/**
 * Created by Dev on 08.02.2018.
 */

public class Stopwatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void start() {
        if(startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        else if(!running) {
            startTime = System.currentTimeMillis() - (stopTime - startTime);
        }
        running = true;
    }


    public void stop() {
        if(running) {
            stopTime = System.currentTimeMillis();
        }
        running = false;
    }

    public void reset() {
        startTime = 0;
        stopTime = 0;
        running = false;
    }

    public void set(long elapsedTime) {
        startTime = System.currentTimeMillis() - elapsedTime;
        stopTime = System.currentTimeMillis();
    }

    // elaspsed time in milliseconds
    public long getElapsedTime() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }

    public String getFormattedElapsedTime() {

        String minutes = Long.toString(getElapsedTime() / 60000 % 60);
        if(minutes.length() < 2) {
            minutes = "0" + minutes;
        }

        String seconds = Long.toString(getElapsedTime() / 1000 % 60);
        if(seconds.length() < 2) {
            seconds = "0" + seconds;
        }

        String hundredths = Long.toString(getElapsedTime() / 10 % 100);
        if(hundredths.length() < 2) {
            hundredths = "0" + hundredths;
        }

        return minutes + ":" + seconds + ":" + hundredths;
    }

    public boolean isRunning() {
        return running;
    }
}
