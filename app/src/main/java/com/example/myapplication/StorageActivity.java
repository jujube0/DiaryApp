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

public class StorageActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    FrameLayout frameLayout;
    boolean isShort = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);


    }

}
