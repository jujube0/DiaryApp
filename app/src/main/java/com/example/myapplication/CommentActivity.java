package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, CommentAdapter.OnRecommentListener {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comment_listView = findViewById(R.id.diary_comment_content_list);
        add_comment=findViewById(R.id.comment_btn_addcomment);
        comment_text=findViewById(R.id.comment_edit_text);
        add_comment.setOnClickListener(this);
        toDiary=findViewById(R.id.diary_comment_btn_toDiary);
        toDiary.setOnClickListener(this);

        comment_layout = findViewById(R.id.diary_comment_layout_recomment_info);
        recomment_info=findViewById(R.id.diary_comment_recomment_info);
        scrollView=findViewById(R.id.diary_comment_scrollView);
        name = getResources().getString(R.string.id);




        CommentAdapter adapter = new CommentAdapter(this, this, name);
        comment_listView.setAdapter(adapter);


        comment_listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


    }
    @Override
    public void onClick(View v) {
        if(v == add_comment){
            if(comment_layout.getVisibility()==View.GONE){
                String comment = comment_text.getText().toString();


                if(!comment.equals("")) {

                    DBHelper helper = new DBHelper(this);
                    SQLiteDatabase db = helper.getWritableDatabase();


                    ContentValues values = new ContentValues();
                    values.put("name", name);
                    values.put("comment", comment);
                    values.put("datetime", System.currentTimeMillis()); //datetime

                    db.insert("tb_comment", null, values);



                    CommentAdapter adapter = new CommentAdapter(this, this, name);
                    comment_listView.setAdapter(adapter);
                    comment_text.setText(null);
                    comment_text.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

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


                    DBHelper helper = new DBHelper(this);
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


                    CommentAdapter adapter = new CommentAdapter(this, this, name);
                    comment_listView.setAdapter(adapter);
                    comment_text.setText(null);
                    comment_listView.expandGroup(parent_pos); // 입력이 완료되면 입력창의 text를 지우고 parent의 댓글창을 엶


                }
            }

        }else if(v==toDiary){
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
        }
    }


    public void ShowToast(String message){
        Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onRecomment(View v, String name, int parent_id, int group_pos) {
        comment_layout.setVisibility(View.VISIBLE);
        delete_comment=findViewById(R.id.diary_comment_recomment_delete);
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
