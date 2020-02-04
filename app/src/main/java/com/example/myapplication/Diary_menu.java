package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Diary_menu extends AppCompatActivity {
//sdfkdslfjs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_menu);

        RecyclerView menuRecyclerView=findViewById(R.id.diary_menu_recyclerView);
        List<DriveDiaryMenu> list = new ArrayList<>();

        DriveDiaryMenu vo = new DriveDiaryMenu();
        vo.title= getResources().getString(R.string.diary_text_title);
        vo.content=getResources().getString(R.string.text);
        vo.heart_num=getResources().getString(R.string.heart_num);
        vo.comment_num=getResources().getString(R.string.comment_num);
        list.add(vo);
        list.add(vo);

        menuRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
        DiaryMenuAdapter adapter = new DiaryMenuAdapter(list);
        menuRecyclerView.setAdapter(adapter);
    }
}
