package com.example.myapplication.Main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.Login.Login;
import com.example.myapplication.Post.Add_long_para;
import com.example.myapplication.R;
import com.example.myapplication.shortPost.Add_short_para;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    static final int NUM_ITEMS = 2;
    ViewPager viewPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;

    private CircleImageView mainImageView;
    private TextView main_textView_name;
    private TextView main_logout;
    private TextView main_info;
 //   private TextView showShortPara;
  //  private TextView showLongPara;
    private Button writeLongPara;
    private Button writeShortPara;
    private FirebaseUser user;
   // private FragmentManager fragmentManager;
    // 한 번만 초기화하면오류남
    //private FragmentTransaction fragmentTransaction;
    //private  boolean isShowingLongPara;
/*    private LongParaMenu LongPara;
    private ShortParaMenu ShortPara;*/


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
        main_info=findViewById(R.id.main_profile_info);

        viewPager = findViewById(R.id.viewPager);

        tabLayout = findViewById(R.id.tabs);
/*
   //     fragmentManager=getSupportFragmentManager();
        shortPara = new ShortParaMenu();
        longPara = new LongParaMenu();
        // 짧은글 보기가 첫 화면.
        switchFragment(shortPara);
//        switchFragment(SHORT_PARA);

        showShortPara=findViewById(R.id.main_show_shortPara);
        showShortPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //longpara를 보여주고 있으면 shortpara로 바꾸고, boolean을 바꿔주고.
                if(isShowingLongPara){
                    switchFragment(new ShortParaMenu());
                    isShowingLongPara=false;
                }
            }
        });
        showLongPara=findViewById(R.id.main_show_longPara);
        showLongPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowingLongPara){
                    switchFragment(longPara);
                    isShowingLongPara=true;
                };
            }
        });*/
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
                Intent intent = new Intent(MainActivity.this, Add_long_para.class);
                startActivity(intent);
            }
        });

        //tablayout구현;
        adapter = new MyPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //progressBar.setVisibility(View.GONE);

    }

/*  framelayout
    private void switchFragment(Fragment fragment) {

        fragmentManager=getSupportFragmentManager();
        //얘는 한 번만 초기화해주면 오류남
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frameLayout, fragment);
        fragmentTransaction.commit();
    }*/

    @Override
    protected void onStart(){
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            // User is signed in
            toLogin();
        }else{
            main_textView_name.setText(user.getDisplayName());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("userInfo");
            ref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String user_info = dataSnapshot.getValue(String.class);
                    main_info.setText(user_info);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            setImage(mainImageView, user.getPhotoUrl());

        }
    }

    private void setImage(CircleImageView mainImageView, Uri photoUrl) {
       Glide.with(MainActivity.this).load(photoUrl.toString()).into(mainImageView);
       /* try {
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUrl);
            mainImageView.setImageBitmap(bitmap);
        }catch (Exception e){

            Toast.makeText(this, "error: "+e , Toast.LENGTH_LONG).show();
        }*/
    }

    public void toLogin(){
        Intent LoginIntent = new Intent(MainActivity.this, Login.class);
        startActivity(LoginIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            fragments.add(new ShortParaMenu());
            fragments.add(new LongParaMenu());
            titles.add("short");
            titles.add("long");
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
