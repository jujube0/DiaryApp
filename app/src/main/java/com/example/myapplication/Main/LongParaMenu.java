package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Post.LongMenuViewHolder;
import com.example.myapplication.Post.ShowLongPost;
import com.example.myapplication.Post.BlogPost;
import com.example.myapplication.R;
import com.example.myapplication.shortPost.ShortPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class LongParaMenu extends Fragment {


    private DatabaseReference ref;
    RecyclerView menuRecyclerView;
    TextView emptyMessage;

    private FirebaseRecyclerAdapter<BlogPost, LongMenuViewHolder> adapter;
  /*  private RecyclerView mRecycler;
    private LinearLayoutManager mManager;*/


    private List<BlogPost> list = new ArrayList<>();
    public LongParaMenu() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.long_para_menu, container, false);
        menuRecyclerView= v.findViewById(R.id.diary_menu_recyclerView);
        emptyMessage=v.findViewById(R.id.long_menu_empty);
        return v;
    }

 /*   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mManager=new LinearLayoutManager(getActivity());
        fetch();
        mRecycler.setLayoutManager(mManager);
    }
*/
    /*private void fetch() {
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query =FirebaseDatabase.getInstance().getReference().child("blogPosts");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) emptyMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<BlogPost> options=
                new FirebaseRecyclerOptions.Builder<BlogPost>()
                        .setQuery(query, BlogPost.class).build();

        adapter = new FirebaseRecyclerAdapter<BlogPost, LongMenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LongMenuViewHolder holder, int position, @NonNull final BlogPost vo) {

                holder.title.setText(vo.title);
                holder.content.setText(vo.content);
                Glide.with(holder.itemView.getContext()).load(vo.img_url).into(holder.imageView);

                holder.date.setText(longToTime(vo.date));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent postIntent = new Intent(holder.itemView.getContext(), ShowLongPost.class);
                        postIntent.putExtra("key", vo.key);
                        holder.itemView.getContext().startActivity(postIntent);
                    }
                });
*//*                holder.content.setText(post.content);
                if(post.img_url!=null){
                    //Glide.with(holder.itemView.getContext()).load(post.img_url).into(holder.imageView);
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext()).load(post.img_url).placeholder(android.R.drawable.progress_indeterminate_horizontal).
                            error(android.R.drawable.stat_notify_error).into(holder.imageView);
                }*//*
            }

            @NonNull
            @Override
            public LongMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_for_long_para, parent, false);
                return new LongMenuViewHolder(view);
            }
        };
        mRecycler.setAdapter(adapter);
    }*/

    public String longToTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MM월 dd일");
        return format.format(date);
    }
    public void onStart() {
        super.onStart();
     //   adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
  //      adapter.stopListening();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("blogPosts").child(user_id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if(dataSnapshot.hasChildren()){
                    emptyMessage.setVisibility(View.GONE);
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        BlogPost post = child.child(""+0).getValue(BlogPost.class);
                        post.key=child.getKey();
                        list.add(post);
                    }
                }
                if(!dataSnapshot.exists()){
                    emptyMessage.setVisibility(View.VISIBLE);
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
        holder.num_comment.setText(""+vo.comment_num);
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
