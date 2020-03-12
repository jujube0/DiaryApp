package com.example.myapplication.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, CommentAdapter.OnRecommentListener {

    //delete가 안됨 시발; delete 후 화면 새로고침 안되고, 삭제하면 다같이 삭제됨. child added 후 expand되는 것도 안됨 하

    private String key;
    private ExpandableListView comment_listView;
    private Button add_comment;
    private EditText comment_text;
    private RelativeLayout comment_layout;
    private Button toDiary;
    private FirebaseUser user;
    private TextView delete_comment;
    private TextView recomment_info;
    private NestedScrollView scrollView;
    private String user_id;
    private String user_name;
    private int comment_num;
    private DatabaseReference ref;

    ArrayList<Comment> parentData;
    HashMap<Integer, ArrayList<Comment>> childData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comment_listView = findViewById(R.id.diary_comment_content_list);
        add_comment=findViewById(R.id.comment_btn_addcomment);
        comment_text=findViewById(R.id.comment_edit_text);
        add_comment.setOnClickListener(this);
        toDiary=findViewById(R.id.diary_comment_btn_toDiary);
        toDiary.setOnClickListener(this);

        comment_layout = findViewById(R.id.diary_comment_layout_recomment_info);
        recomment_info=findViewById(R.id.diary_comment_recomment_info);
        scrollView=findViewById(R.id.diary_comment_scrollView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id= user.getUid();
        user_name=user.getDisplayName();
        parentData=new ArrayList<>();
        childData=new HashMap<>();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
     //   comment_num=intent.getIntExtra("comment_num", 0);

        //Create list
        comment_num=0;
        ref = FirebaseDatabase.getInstance().getReference().child("comments").child(user_id).child(key);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CreateComment(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        comment_listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 댓글 개수를 넣어주기
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("blogPosts").child(user_id).child(key);
        postRef.child("0").child("comment_num").setValue(comment_num);
    }

    @Override
    public void onClick(View v) {
        if(v == add_comment){
            // 대댓글이 아니면,
            if(comment_layout.getVisibility()==View.GONE){
                String content = comment_text.getText().toString();

                if(!content.equals("")) {
                    //int comment_id, String user_id, String user_name, String content, long date
                    Comment c = new Comment(comment_num, user_id, user_name, content, System.currentTimeMillis());
                    ref.push().setValue(c);
                  //  comment_num++;

                    comment_text.setText(null);
                    comment_text.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                }
                //대댓글이면,
            }else{
                String content = comment_text.getText().toString();

                if(!content.equals("")) {

                    ArrayList<Integer> value = (ArrayList<Integer>) v.getTag();

                    int parent_id = value.get(0);
                    int parent_pos = value.get(1);
                    Comment c = new Comment(comment_num, user_id, user_name, content, System.currentTimeMillis(), parent_id);
                    ref.push().setValue(c);

                    comment_text.setText(null);
                    comment_listView.expandGroup(parent_pos); // 입력이 완료되면 입력창의 text를 지우고 parent의 댓글창을 엶


                }
            }
        //본문보기
        }else if(v==toDiary){
            finish();
        }
    }


    public void ShowToast(String message){
        Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onRecomment(View v, String name, int parent_id, int group_pos) {
        comment_layout.setVisibility(View.VISIBLE);
        delete_comment=findViewById(R.id.diary_comment_recomment_delete);
        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_layout.setVisibility(View.GONE);
            }
        });
        String recomment = name +"님에게 답글을 남기는 중...";
        recomment_info.setText(recomment);
        ArrayList<Integer> value = new ArrayList<>();
        value.add(parent_id);
        value.add(group_pos);
        add_comment.setTag(value);

    }

    void CreateComment(DataSnapshot dataSnapshot){
        Comment co = dataSnapshot.getValue(Comment.class);
        //parent 이면,
        if(co.parent_id == -1){
            parentData.add(co);
            ArrayList<Comment> child = new ArrayList<>();
            childData.put(co.comment_id, child);
            //child이면
        }else{
            childData.get(co.parent_id).add(co);
        }
        //Context context, OnRecommentListener mlistener, FirebaseUser user, ArrayList<Comment> parentData,HashMap<Integer, ArrayList<Comment>> childData
        CommentAdapter adapter = new CommentAdapter(CommentActivity.this, CommentActivity.this, user, parentData, childData, key);
        comment_listView.setAdapter(adapter);
        comment_num++;
    }
}
