package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryMenuViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView content;
    public TextView num_heart;
    public TextView num_comment;

    public DiaryMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.diary_menu_title);
        content=itemView.findViewById(R.id.diary_menu_content);
        num_heart=itemView.findViewById(R.id.diary_text_heart_num);
        num_comment=itemView.findViewById(R.id.diary_text_comment_num);
    }
}
