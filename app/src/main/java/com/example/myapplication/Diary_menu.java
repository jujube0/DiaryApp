package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Diary_menu extends AppCompatActivity {

    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_menu);

        final List<BlogPost> list = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("blogPosts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        BlogPost post = child.child(""+0).getValue(BlogPost.class);
                        list.add(post);
                    }
                }

                RecyclerView menuRecyclerView=findViewById(R.id.diary_menu_recyclerView);
                menuRecyclerView.setLayoutManager(new LinearLayoutManager((Diary_menu.this)));
                DiaryMenuAdapter adapter = new DiaryMenuAdapter(list);
                menuRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public class DiaryMenuAdapter extends RecyclerView.Adapter<DiaryMenuViewHolder>{

        private List<BlogPost> list;


        public DiaryMenuAdapter( List<BlogPost> list){
            this.list=list;
        }


        @Override
        public DiaryMenuViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.diary_menu_recyclerview_item, parent,false);
            return new DiaryMenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final DiaryMenuViewHolder holder, int position) {
            final BlogPost vo = list.get(position);

            holder.title.setText(vo.title);
            holder.content.setText(vo.content);
            Glide.with(holder.itemView.getContext()).load(vo.img_url).into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


}
