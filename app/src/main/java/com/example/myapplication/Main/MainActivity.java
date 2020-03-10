package com.example.myapplication.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.myapplication.InformationActivity;
import com.example.myapplication.Login.Login;
import com.example.myapplication.MenuFragment;
import com.example.myapplication.Post.TextActivity;
import com.example.myapplication.R;
import com.example.myapplication.shortPost.Add_short_para;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mainImageView;
    private TextView main_textView_name;
    private TextView main_logout;
    private TextView showShortPara;
    private TextView showLongPara;
    private Button writeLongPara;
    private Button writeShortPara;

    private FirebaseUser user;
    private FragmentManager fragmentManager;
    // 한 번만 초기화하면오류남
    private FragmentTransaction fragmentTransaction;
    private  boolean isShowingLongPara;
/*    private LongParaMenu LongPara;
    private ShortParaMenu ShortPara;*/

    ShortParaMenu shortPara;
    LongParaMenu longPara;
    private final int LONG_PARA = 1;
    private final int SHORT_PARA = 2;


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

        fragmentManager=getSupportFragmentManager();
        shortPara = new ShortParaMenu();
        longPara = new LongParaMenu();
        // 짧은글 보기가 첫 화면.
        isShowingLongPara=false;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack(null);
        ft.add(R.id.main_frameLayout, shortPara);
        ft.commit();
//        switchFragment(SHORT_PARA);

        showShortPara=findViewById(R.id.main_show_shortPara);
        showShortPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(SHORT_PARA);
            }
        });
        showLongPara=findViewById(R.id.main_show_longPara);
        showLongPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(LONG_PARA);
            }
        });

      //  fragmentTransaction.replace(R.id.main_frameLayout, ShortPara).commitAllowingStateLoss();
      //  isShowingLongPara=false;
        //글쓰기 버튼
        writeShortPara = findViewById(R.id.main_btn_writeShort);
        writeShortPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_short_para.class);
                startActivity(intent);
            }
        });
        writeLongPara=findViewById(R.id.main_btn_writeLong);
        writeLongPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextActivity.class);
                startActivity(intent);
            }
        });
    }
// 매개변수는 눌리는 버튼의 종류

    private void switchFragment(int Para) {

        //얘는 한 번만 초기화해주면 오류남

        //fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        //현재 longpara를 보여주지 않고 있는데 longpara버튼이 눌린다면 longpara를 보여주기
        if(!isShowingLongPara&&Para==LONG_PARA){
            fragmentTransaction.addToBackStack(null);
           // fragmentTransaction.replace(R.id.main_frameLayout, new MenuFragment()).commitAllowingStateLoss();
            fragmentTransaction.replace(R.id.main_frameLayout, new LongParaMenu());
            fragmentTransaction.commitAllowingStateLoss();
            isShowingLongPara=true;
        }else if(isShowingLongPara&&Para==SHORT_PARA){

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.main_frameLayout, shortPara);
            fragmentTransaction.commit();
            isShowingLongPara=false;
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
        }

    }

}
