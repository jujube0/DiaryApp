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

   /* public BlogPost(String author_account, String title, long date) {
        this.author_account = author_account;
        this.title = title;
        this.date = date;
    }*/
    public BlogPost(String author_account, String title, String category, long date) {
        this.author_account = author_account;
        this.title = title;
        this.category = category;
        this.date = date;
    }
}
