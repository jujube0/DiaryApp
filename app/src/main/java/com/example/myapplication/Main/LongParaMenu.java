package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.myapplication.DiaryMenuViewHolder;
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

    public LongParaMenu() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.long_para_menu, container);

        final List<BlogPost> list = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("blogPosts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        BlogPost post = child.child(""+0).getValue(BlogPost.class);
                        list.add(post);
                    }
                }

                RecyclerView menuRecyclerView=v.findViewById(R.id.diary_menu_recyclerView);
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

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
class DiaryMenuAdapter extends RecyclerView.Adapter<DiaryMenuViewHolder>{

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