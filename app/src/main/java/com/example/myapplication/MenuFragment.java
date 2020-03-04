package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
/*
    private RecyclerView mRecyclerView;
    private Diary_MenuAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<DriveDiaryMenu> list = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_diary_menu, container);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.diary_menu_recyclerView);

        List<DriveDiaryMenu> list = new ArrayList<>();

        DriveDiaryMenu vo = new DriveDiaryMenu();
        vo.title= getResources().getString(R.string.diary_text_title);
        vo.content=getResources().getString(R.string.text);
        vo.heart_num=getResources().getString(R.string.heart_num);
        vo.comment_num=getResources().getString(R.string.comment_num);
        list.add(vo);
        list.add(vo);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        adapter = new Diary_MenuAdapter(list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}

class Diary_MenuAdapter extends RecyclerView.Adapter<DiaryMenuViewHolder> {

    private List<DriveDiaryMenu> list;


    public Diary_MenuAdapter(List<DriveDiaryMenu> list) {
        this.list = list;
    }


    @Override
    public DiaryMenuViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_menu_recyclerview_item, parent, false);

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
    }*/
}

