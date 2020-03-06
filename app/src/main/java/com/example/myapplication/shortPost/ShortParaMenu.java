package com.example.myapplication.shortPost;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Post.BlogPost;
import com.example.myapplication.Post.DiaryActivity;
import com.example.myapplication.Post.DiaryMenuViewHolder;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ShortParaMenu extends Fragment {
    private DatabaseReference ref;
    ShortParaMenu(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
class ShortMenuHolders extends RecyclerView.ViewHolder{
    public TextView content;
    public TextView showMore;
    public ImageView imageView;


    public ShortMenuHolders(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.menu_short_imageView);
        content=itemView.findViewById(R.id.menu_short_textView);
        showMore=itemView.findViewById(R.id.menu_short_showMore);
    }
}
class ShortMenuAdapter extends RecyclerView.Adapter<ShortMenuHolders>{

    private List<ShortPost> list;
    public ShortMenuAdapter(List<ShortPost> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ShortMenuHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_for_short_para, parent, false);
        return new ShortMenuHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortMenuHolders holder, int position) {
        final ShortPost po = list.get(position);
        holder.content.setText(po.content);
        if(po.img_url!=null){
            Glide.with(holder.itemView.getContext()).load(po.img_url).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
