package com.viper.team.encampus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String data;
    private String sender;
    private String recv;
    private boolean read;
    private String timestamp;

    public Message()
    {

    }


    public Message(String m, String s, String r) {
        data = m;
        sender = s;
        recv = r;
        read = false;
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        Date date = new Date();
        timestamp=dateFormat.format(date);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {

        this.data = data;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {

        this.sender = sender;
    }

    public String getRecv()
    {
        return recv;
    }

    public void setRecv(String recv)
    {
        this.recv = recv;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
