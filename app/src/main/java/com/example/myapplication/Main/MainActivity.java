package com.example.myapplication.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Post.DiaryActivity;
import com.example.myapplication.InformationActivity;
import com.example.myapplication.Login.Login;
import com.example.myapplication.Post.TextActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mainImageView;
    private TextView main_textView_name;
    private TextView main_btn_blog;
    private TextView main_logout;
    private TextView showShortPara;
    private TextView showLongPara;
    private Button writeLongPara;

    private FirebaseUser user;
    private FragmentManager fragmentManager;
    private  FragmentTransaction fragmentTransaction;
    private  boolean isShowingLongPara;
    private LongParaMenu LongPara;

    private final int LONG_PARA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainImageView=findViewById(R.id.main_profile_pic);
        mainImageView.setOnClickListener(this);
        main_textView_name=findViewById(R.id.main_name);
        main_textView_name.setOnClickListener(this);
        main_logout = findViewById(R.id.main_logout);
        main_logout.setOnClickListener(this);
        showShortPara=findViewById(R.id.main_show_shortPara);
        showShortPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        });
        showLongPara=findViewById(R.id.main_show_longPara);
        showLongPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        });
        fragmentManager=getSupportFragmentManager();
        LongPara = new LongParaMenu();
        fragmentTransaction=fragmentManager.beginTransaction();
        isShowingLongPara=true;
        fragmentTransaction.replace(R.id.main_frameLayout, LongPara).commitAllowingStateLoss();
        writeLongPara=findViewById(R.id.main_btn_writeLong);
        writeLongPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextActivity.class);
                startActivity(intent);
            }
        });
    }

    private void switchFragment() {
        if(!isShowingLongPara){
            fragmentTransaction.replace(R.id.main_frameLayout, LongPara).commitAllowingStateLoss();
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        user =FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            // User is signed in
            toLogin();
        }else{
            main_textView_name.setText(user.getDisplayName());
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
