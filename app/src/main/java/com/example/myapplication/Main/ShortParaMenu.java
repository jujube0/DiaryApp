package com.example.myapplication.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.shortPost.ShortPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShortParaMenu extends Fragment {

    private FirebaseRecyclerAdapter<ShortPost, ShortMenuHolders> adapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private TextView empty_message;

    public ShortParaMenu() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.short_para_menu, container, false);
        empty_message=rootView.findViewById(R.id.short_menu_empty);
        mRecycler=rootView.findViewById(R.id.short_menu_recycler);
        empty_message.setVisibility(View.GONE);

        //mRecycler.setHasFixedSize(true);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mManager=new LinearLayoutManager(getActivity());
        fetch();
        mRecycler.setLayoutManager(mManager);
    }

    private void fetch() {
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("shortPosts").child(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) empty_message.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<ShortPost> options=
                new FirebaseRecyclerOptions.Builder<ShortPost>()
                .setQuery(query, ShortPost.class).build();

        adapter = new FirebaseRecyclerAdapter<ShortPost, ShortMenuHolders>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShortMenuHolders holder, int position, @NonNull ShortPost post) {

                holder.content.setText(post.content);
                holder.date.setText(longToTime(post.date));
                if(post.img_url!=null){
                    //Glide.with(holder.itemView.getContext()).load(post.img_url).into(holder.imageView);
                    holder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext()).load(post.img_url).placeholder(android.R.drawable.progress_indeterminate_horizontal).
                    error(android.R.drawable.stat_notify_error).into(holder.imageView);
                }
            }

            @NonNull
            @Override
            public ShortMenuHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_for_short_para, parent, false);
                return new ShortMenuHolders(view);
            }
        };
        mRecycler.setAdapter(adapter);
    }

    public String longToTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MM월 dd일");
        return format.format(date);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
//
//    private DatabaseReference ref;
//    private List<ShortPost> list;
//    RecyclerView menuRecyclerView;
//    ShortMenuAdapter adapter;
//
//    public ShortParaMenu(){}
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.short_para_menu, container);
//        menuRecyclerView=v.findViewById(R.id.short_menu_recycler);
//        list = new ArrayList<>();
//        ref= FirebaseDatabase.getInstance().getReference().child("shortPosts");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.hasChildren()){
//                    for(DataSnapshot child: dataSnapshot.getChildren()){
//                        ShortPost shortPost = child.getValue(ShortPost.class);
//                        list.add(shortPost);
//                    }
//                }
//                final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                menuRecyclerView.setLayoutManager(mLayoutManager);
//                adapter = new ShortMenuAdapter(list);
//                menuRecyclerView.setAdapter(adapter);
//                menuRecyclerView.setItemAnimator(new DefaultItemAnimator());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        if(adapter!=null){
//        }
//    }
//}
class ShortMenuHolders extends RecyclerView.ViewHolder{
    public TextView content;
    public TextView showMore;
    public ImageView imageView;
    public TextView date;


    public ShortMenuHolders(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.menu_short_imageView);
        content=itemView.findViewById(R.id.menu_short_textView);
        showMore=itemView.findViewById(R.id.menu_short_showMore);
        date=itemView.findViewById(R.id.menu_short_date);

    }
}
//
//class ShortMenuAdapter extends RecyclerView.Adapter<ShortMenuHolders>{
//
//    private List<ShortPost> list;
//    public ShortMenuAdapter(List<ShortPost> list) {
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public ShortMenuHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_for_short_para, parent, false);
//        return new ShortMenuHolders(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ShortMenuHolders holder, int position) {
//        final ShortPost po = list.get(position);
//        holder.content.setText(po.content);
//        if(po.img_url!=null){
//            Glide.with(holder.itemView.getContext()).load(po.img_url).placeholder(android.R.drawable.progress_indeterminate_horizontal).
//                    error(android.R.drawable.stat_notify_error).into(holder.imageView);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//}
