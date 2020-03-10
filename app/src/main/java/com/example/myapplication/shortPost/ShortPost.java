package com.example.myapplication.shortPost;

import java.sql.Timestamp;

public class ShortPost {
    public long date;
    public String img_url;
    public String content;

    public ShortPost(){}
    public ShortPost(long date, String img_url, String content) {
        this.date = date;
        this.img_url = img_url;
        this.content = content;
    }

    public ShortPost(long date, String content) {
        this.date = date;
        this.content = content;
    }

    public ShortPost(String date, String content) {
        this.content=content;
        this.date=Long.parseLong(date);
    }
}
