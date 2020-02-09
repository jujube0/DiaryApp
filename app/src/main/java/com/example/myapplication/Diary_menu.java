package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public class DiaryMenuAdapter extends RecyclerView.Adapter<DiaryMenuViewHolder>{

        private List<DriveDiaryMenu> list;


        public DiaryMenuAdapter( List<DriveDiaryMenu> list){
            this.list=list;
        }


        @Override
        public DiaryMenuViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.diary_menu_recyclerview_item, parent,false);

            return new DiaryMenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiaryMenuViewHolder holder, int position) {
            final DriveDiaryMenu vo = list.get(position);

            holder.title.setText(vo.title);
            holder.content.setText(vo.content);
            //  holder.img.setImageDrawable((Drawable) vo.imageGetter);   ///????????????????
            holder.num_comment.setText(vo.comment_num);
            holder.num_heart.setText(vo.heart_num);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
