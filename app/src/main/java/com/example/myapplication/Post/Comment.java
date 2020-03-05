package com.example.myapplication.Post;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Comment {
    // 댓글 쓴 user 의 email
    public String author_account;
    //댓글의 _id
    public int comment_id;
    // 댓글 내용
    public String content;
    //삭제여부
    public boolean deleted = false;
    //수정여부
    public boolean modifed = false;
    //댓글작성시간
    public long date;
    //댓글 부모id
    public int parent_id;

    private DatabaseReference mDatabase;

    public Comment(){}

    public Comment(int comment_id, String author_account, String content, long date) {
        this.comment_id=comment_id;
        this.author_account=author_account;
        this.content = content;
        this.date = date;
        this.parent_id = -1;

    }
    public Comment(int comment_id, String author_account, String content, long date, int parent_id) {
        this.comment_id=comment_id;
        this.author_account=author_account;
        this.content = content;
        this.date = date;
        this.parent_id = parent_id;

    }


}
