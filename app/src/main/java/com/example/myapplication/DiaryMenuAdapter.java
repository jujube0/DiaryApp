package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
