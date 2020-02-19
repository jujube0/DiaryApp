package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DiaryComment extends AppCompatActivity{

    TextView btn1, btn2;
    MenuFragment frag_menu;
    Frag_comment frag_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_comment);
        btn1 = findViewById(R.id.diary_comment_btn_1);
        btn2 = findViewById(R.id.diary_comment_btn_2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFrag(2);
            }
        });
        frag_menu = new MenuFragment();
        frag_comment = new Frag_comment();
        setFrag(1);

    }
    public void setFrag(int n){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tran = fm.beginTransaction();
        if(n == 1){
            tran.replace(R.id.Diary_frameLayout, frag_menu);
            tran.commit();
        }else{
            tran.replace(R.id.Diary_frameLayout, frag_comment);
            tran.commit();
        }
    }


}
