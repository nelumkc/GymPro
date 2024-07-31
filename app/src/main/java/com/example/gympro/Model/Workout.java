package com.example.gympro.Model;

public class Workout {
    private String date;
    private long duration;

    private String name;

    public Workout(String date, long duration, String name) {
        this.date = date;
        this.duration = duration;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
