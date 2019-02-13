package com.viper.team.encampus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reminder
{
    String date,data,created,user,time,name;
    public Reminder(){}

    public Reminder(String date, String data, String user, String time, String name) {
        this.date = date;
        this.data = data;
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        Date created = new Date();
        this.created=dateFormat.format(created);
        this.user = user;
        this.time = time;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
