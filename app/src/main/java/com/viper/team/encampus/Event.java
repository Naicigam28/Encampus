package com.viper.team.encampus;


import android.graphics.Color;

public class Event extends com.github.sundeepk.compactcalendarview.domain.Event {

    private int color;
    private long timeInMillis;
    String start,end,notes,name;
    private EventData data;

    public Event() {
        super(1,0,null);
    }

    public Event(String start, String end, String notes, String name) {
        //super(color, timeInMillis, data);
        this();
        this.setColor(Color.GREEN);
        this.start = start;
        this.end = end;
        this.notes = notes;
        this.name = name;
    }

    public Event(int color, long timeInMillis) {
        this();
        this.color = color;
        this.timeInMillis = timeInMillis;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public void setData(EventData data) {
        this.data = data;
    }

    public Event(int color, long timeInMillis, EventData data) {
        this();
        this.color = color;
        this.timeInMillis = timeInMillis;
        this.data = data;
    }

    public int getColor() {
        return color;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }


    public EventData getEventData() {
        return data;
    }


    @Override
    public int hashCode() {
        int result = color;
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return start+" - "+end+"\n"+name+"\n"+notes +"\n "+timeInMillis;
    }
}


