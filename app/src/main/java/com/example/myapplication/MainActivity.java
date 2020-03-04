package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView mainImageView;
    TextView main_textView_name;
    TextView main_btn_blog;
    TextView main_logout;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainImageView=findViewById(R.id.main_pic_parent_view);
        mainImageView.setOnClickListener(this);
        main_textView_name=findViewById(R.id.main_name_title_view);
        main_textView_name.setOnClickListener(this);
        main_btn_blog=findViewById(R.id.main_btn_blog);
        main_btn_blog.setOnClickListener(this);
        main_logout = findViewById(R.id.main_logout);
        main_logout.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        main_textView_name.setText(name);

    }
    @Override
    protected void onStart(){
        super.onStart();
        user =FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            // User is signed in
            toLogin();
        }
    }
    public void toLogin(){
        Intent LoginIntent = new Intent(MainActivity.this, Login.class);
        startActivity(LoginIntent);
        finish();
    }
    @Override
    public void onClick(View v) {
        if(v==mainImageView||v==main_textView_name){
            Intent intent = new Intent(this, InformationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if(v==main_logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
            toLogin();

        }else if(v==main_btn_blog){
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
        }

    }
}
