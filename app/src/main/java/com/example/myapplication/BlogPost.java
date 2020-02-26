package com.example.myapplication;

import java.sql.Timestamp;

public class BlogPost {
    // content의 순서. title은 0
    public int num;
    //작성자
    public String author_account;
    public Timestamp date;
    public String title;
    public String category;

    public String img_url;
    public String content;
    public int type;
    public static final int TITLE = 0;
    public static final int TEXT = 1;
    public static final int IMAGE = 2;

    public BlogPost(){}



    public BlogPost(String author_account, String title, String category, Timestamp date) {
        this.num = 0;
        this.author_account = author_account;
        this.title = title;
        this.category = category;
        this.date = date;
        this.type = TITLE;
    }
    public BlogPost(int num, int type,  String content){
        this.num = num;
        this.type = type;
        this.content = content;
    }
    public BlogPost(int name, int type){
        this.num = num;
        this.type = type;
    }
    public void putContent(String content){
        this.content = content;
    }

}
