package com.viper.team.encampus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.AppCompatImageHelper;
import android.util.Base64;

public class Book
{
    private String title,author,edition,year,price;
    private long picID;

    public Book(){}

    public Book(String title, String author,String edition, String year, String price, long pic) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.picID = pic;
        this.edition=edition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPic() {
        return picID;
    }

    public void setPic(int pic) {
        this.picID = pic;
    }
    @Override
    public String toString()
    {
        return this.title+"\n"+this.author+"\n"+this.edition+"\n"+this.year+"\n"+"R"+this.price;
    }

}
