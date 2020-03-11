package com.example.myapplication.Post;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class LongMenuViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView content;
    public TextView num_heart;
    public TextView num_comment;
    public ImageView imageView;


    public LongMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.diary_menu_pic);
        title=itemView.findViewById(R.id.diary_menu_title);
        content=itemView.findViewById(R.id.diary_menu_content);
        num_heart=itemView.findViewById(R.id.diary_text_heart_num);
        num_comment=itemView.findViewById(R.id.diary_text_comment_num);

    }
}
