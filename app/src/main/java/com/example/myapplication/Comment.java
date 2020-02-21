package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Comment {
    // 댓글 쓴 user 의 email

    public String comment_writer;
    public String content;
    public boolean deleted = false;
    public boolean modifed = false;
    public long date;
    //parent_id를 가짐. 자신이 parent인 경우에는 -1
    public int parent_comment_id = -1;

    private DatabaseReference mDatabase;

    public Comment(){}

    public Comment(String comment_writer, String content, long date) {
        this.comment_writer=comment_writer;
        this.content = content;
        this.date = date;
    }

    public Comment(String comment_writer, String content, long date, int parent_comment_id) {
        this.comment_writer=comment_writer;
        this.content = content;
        this.date = date;
        this.parent_comment_id = parent_comment_id;
    }

}
