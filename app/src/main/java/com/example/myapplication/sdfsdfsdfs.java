package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.myapplication.Main.LongParaMenu;
import com.example.myapplication.Main.ShortParaMenu;

public class sdfsdfsdfs extends AppCompatActivity {

    Button btn1;
    Button btn2;
    FrameLayout frameLayout;
    boolean isShort = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdfsdfsdfs);
        btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        frameLayout=findViewById(R.id.framelayout);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment();
            }
        });

    }

    private void switchFragment() {
        Fragment fr;
        if(isShort){
            fr = new ShortParaMenu();
        }else {
            fr=new LongParaMenu();
        }
        isShort=(isShort)?false:true;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, fr);
        fragmentTransaction.commit();
    }
}
