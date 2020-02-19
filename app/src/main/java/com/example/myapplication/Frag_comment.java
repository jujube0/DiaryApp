package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Frag_comment extends Fragment implements View.OnClickListener, CommentAdapter.OnRecommentListener {

    View rootView;
    ExpandableListView comment_listView;
    Button add_comment;
    EditText comment_text;
    RelativeLayout comment_layout;
    Button toDiary;

    TextView delete_comment;
    TextView recomment_info;
    NestedScrollView scrollView;
    String name;

    ArrayList<DriveComment> parentData = new ArrayList<>();
    ArrayList<ArrayList<DriveComment>> childData = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_comment, container,false);

        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        comment_listView = (ExpandableListView)view.findViewById(R.id.diary_comment_content_list);

        comment_listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        add_comment=view.findViewById(R.id.comment_btn_addcomment);
        comment_text=view.findViewById(R.id.comment_edit_text);
        add_comment.setOnClickListener(this);
        toDiary=view.findViewById(R.id.diary_comment_btn_toDiary);
        toDiary.setOnClickListener(this);

        delete_comment=view.findViewById(R.id.diary_comment_recomment_delete);

        comment_layout = view.findViewById(R.id.diary_comment_layout_recomment_info);
        recomment_info=view.findViewById(R.id.diary_comment_recomment_info);
        scrollView=view.findViewById(R.id.diary_comment_scrollView);
        name = getResources().getString(R.string.id);




        CommentAdapter adapter = new CommentAdapter(getActivity(), this, name);
        comment_listView.setAdapter(adapter);

    }

        @Override
    public void onClick(View v) {
        if(v == add_comment){
            if(comment_layout.getVisibility()==View.GONE){
                String comment = comment_text.getText().toString();


                if(!comment.equals("")) {

                    DBHelper helper = new DBHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();


                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("comment", comment);
                    values.put("datetime", System.currentTimeMillis()); //datetime

                    db.insert("tb_comment", null, values);



                    CommentAdapter adapter = new CommentAdapter(getActivity(), this, name);
                    comment_listView.setAdapter(adapter);
                    comment_text.setText(null);
                    comment_text.clearFocus();
                    /*
                    //키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/

                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                }
            }else{
                String comment = comment_text.getText().toString();
                String name = getResources().getString(R.string.id);

                if(!comment.equals("")) {


                    DBHelper helper = new DBHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ArrayList<Integer> value = (ArrayList<Integer>) v.getTag();


                    String parent_id = value.get(0).toString();
                    int parent_pos = value.get(1);

                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("comment", comment);
                    values.put("parent", parent_id);
                    values.put("datetime", System.currentTimeMillis());

                    db.insert("tb_comment", null, values);


                    CommentAdapter adapter = new CommentAdapter(getActivity(), this, name);
                    comment_listView.setAdapter(adapter);
                    comment_text.setText(null);
                    comment_listView.expandGroup(parent_pos); // 입력이 완료되면 입력창의 text를 지우고 parent의 댓글창을 엶


                }
            }

        }
    }


    public void ShowToast(String message){
        Toast t = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onRecomment(View v, String name, int parent_id, int group_pos) {
        comment_layout.setVisibility(View.VISIBLE);
        delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_layout.setVisibility(View.GONE);
            }
        });
        String recomment = name +"님에게 답글을 남기는 중...";
        recomment_info.setText(recomment);
        ArrayList<Integer> value = new ArrayList<>();
        value.add(parent_id);
        value.add(group_pos);
        add_comment.setTag(value);

    }
}
