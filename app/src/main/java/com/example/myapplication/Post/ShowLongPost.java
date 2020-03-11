package com.example.myapplication.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowLongPost extends AppCompatActivity implements View.OnClickListener {

    private String key;
    private LinearLayout layout;
    private LinearLayout.LayoutParams layoutParams;

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
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        title = findViewById(R.id.diary_text_title);
        category = findViewById(R.id.diary_top_category);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("blogPosts").child(key);
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
      /*  ImageView imageView = new ImageView(ShowLongPost.this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(ShowLongPost.this).load(url).into(imageView);
        layout.addView(imageView, layoutParams);*/

      ImageView imageView = new ImageView(ShowLongPost.this);
      imageView.setVisibility(View.VISIBLE);
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setLayoutParams(layoutParams);
      Glide.with(ShowLongPost.this).load(url).into(imageView);
      layout.addView(imageView);
    }


    private void addTextView(String content) {
        TextView textView = new TextView(ShowLongPost.this);
        textView.setText(content);
        layout.addView(textView, layoutParams);
    }
    @Override
    public void onClick(View view) {
        if(view == commentBtn){
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);
        }
    }
}

