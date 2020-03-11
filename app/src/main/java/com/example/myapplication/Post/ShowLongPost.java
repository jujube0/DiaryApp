package com.example.myapplication.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowLongPost extends AppCompatActivity implements View.OnClickListener {

    private String key;
    private LinearLayout layout;
    private LinearLayout.LayoutParams textParams;
    private LinearLayout.LayoutParams imageParams;

    private ImageView commentBtn;
    private TextView category;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        commentBtn = findViewById(R.id.diary_image_comment);
        commentBtn.setOnClickListener(this);
        layout = findViewById(R.id.diary_layout_content);

        textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        //value를 dp로 바꾸는 법

        imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, toDp(350)
        );
        //layoutParams=new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);
        title = findViewById(R.id.diary_text_title);
        category = findViewById(R.id.diary_top_category);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        String user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("blogPosts").child(user_id).child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        BlogPost p = child.getValue(BlogPost.class);
                        if (p.type == BlogPost.TITLE) {
                            title.setText(p.title);
                            category.setText(p.category);
                        }else if(p.type == BlogPost.TEXT){
                            addTextView(p.content);
                        }else if(p.type == BlogPost.IMAGE){
                            addImageView(p.img_url);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addImageView(String url) {
      ImageView imageView = new ImageView(ShowLongPost.this);
      imageParams.setMargins(0,toDp(10),0,toDp(10));
      Glide.with(this).load(url).into(imageView);
      layout.addView(imageView, imageParams);
    }


    private int toDp(int value){
       return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());

    }


    private void addTextView(String content) {
        TextView textView = new TextView(ShowLongPost.this);
        textView.setText(content);
        layout.addView(textView, textParams);
    }
    @Override
    public void onClick(View view) {
        if(view == commentBtn){
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);
        }
    }
}

