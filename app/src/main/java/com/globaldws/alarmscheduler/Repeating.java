package com.globaldws.alarmscheduler;

public class Repeating {

    private int id;
    private String Title;
    private long Period;

    public Repeating(int id, String title, long period) {
        this.id = id;
        Title = title;
        Period = period;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public long getPeriod() {
        return Period;
    }

    public void setPeriod(long period) {
        Period = period;
    }
}
