package com.example.myapplication.Post;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class Comment {
    // 댓글 쓴 user 의 id
    public String user_id;
    // user name
    public String user_name;
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

    public Comment(int comment_id, String user_id, String user_name, String content, long date) {
        this.comment_id=comment_id;
        this.content = content;
        this.user_id=user_id;
        this.user_name=user_name;
        this.date = date;
        this.parent_id = -1;

    }
    public Comment(int comment_id, String user_id,String user_name, String content, long date, int parent_id) {
        this.comment_id=comment_id;
        this.user_id=user_id;
        this.user_name=user_name;
        this.content = content;
        this.date = date;
        this.parent_id = parent_id;

    }


}
