package com.example.myapplication;

public class BlogPost {
    //작성자
    public String author_account;
    public String img_url;
    public long date;

    public String title;
    public String content;
    public String category;

    public BlogPost(){}

    public BlogPost(String author_account, String title, String content, long date) {
        this.author_account = author_account;
        this.title = title;
        this.content = content;
        this.date = date;
    }
    public BlogPost(String author_account, String title, String content, String category, long date) {
        this.author_account = author_account;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
    }
}
