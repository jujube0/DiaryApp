package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.myapplication.Post.LongMenuViewHolder;
import com.example.myapplication.Post.ShowLongPost;
import com.example.myapplication.Post.BlogPost;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class LongParaMenu extends Fragment {


    private DatabaseReference ref;
    RecyclerView menuRecyclerView;


    private List<BlogPost> list = new ArrayList<>();
    public LongParaMenu() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.long_para_menu, container, false);
        menuRecyclerView= v.findViewById(R.id.diary_menu_recyclerView);


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ref = FirebaseDatabase.getInstance().getReference().child("blogPosts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        BlogPost post = child.child(""+0).getValue(BlogPost.class);
                        post.key=child.getKey();
                        list.add(post);
                    }
                }

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                menuRecyclerView.setLayoutManager(mLayoutManager);
                DiaryMenuAdapter adapter = new DiaryMenuAdapter(list);
                menuRecyclerView.setAdapter(adapter);
                menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


class DiaryMenuAdapter extends RecyclerView.Adapter<LongMenuViewHolder>{

    private List<BlogPost> list;


    public DiaryMenuAdapter( List<BlogPost> list){
        this.list=list;
    }


    @Override
    public LongMenuViewHolder onCreateViewHolder(final ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_for_long_para, parent,false);

        return new LongMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LongMenuViewHolder holder, final int position) {
        final BlogPost vo = list.get(position);
        holder.title.setText(vo.title);
        holder.content.setText(vo.content);
        Glide.with(holder.itemView.getContext()).load(vo.img_url).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(holder.itemView.getContext(), ShowLongPost.class);
                postIntent.putExtra("key", vo.key);
                holder.itemView.getContext().startActivity(postIntent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}