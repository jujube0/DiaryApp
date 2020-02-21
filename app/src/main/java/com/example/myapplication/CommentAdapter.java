package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CommentAdapter extends BaseExpandableListAdapter {
    Context context;
    String user_id;

    ArrayList<Comment> parentData = new ArrayList<>();
    ArrayList<String> parent_id = new ArrayList<>();
    ArrayList<ArrayList<Comment>> childData = new ArrayList<>();
    private OnRecommentListener mlistener;
    private DatabaseReference mDatabase;
    public interface OnRecommentListener {
        void onRecomment(View v, String name, int parent_id, int group_pos);
    }




    public CommentAdapter(Context context, OnRecommentListener mlistener, String user_id) {
        super();
        this.context = context;
        this.user_id = user_id;
        this.mlistener=mlistener;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("comments");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Comment c = dataSnapshot.getValue(Comment.class);
                parentData.add(c);
                parent_id.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getGroupCount() {
        return parentData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) { // childeren 수
        return childData.get(groupPosition).size();
    }

    @Override
    public Comment getGroup(int groupPosition) {
        return parentData.get(groupPosition);
    }

    @Override // get information of player
    public Comment getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return 0;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Comment vo = (Comment)getChild(groupPosition, childPosition);
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.diary_comment_listview, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.comment_list_name);
            viewHolder.comment = convertView.findViewById(R.id.comment_list_content);
            viewHolder.expandBtn=convertView.findViewById(R.id.comment_list_show_recomment);
            viewHolder.add_recomment=convertView.findViewById(R.id.comment_list_make_recomment);
            viewHolder.edit_btn=convertView.findViewById(R.id.comment_list_delete);
            viewHolder.datetime=convertView.findViewById(R.id.comment_list_datetime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Comment co = getGroup(groupPosition);

        viewHolder.name.setText(co.comment_writer);
        viewHolder.comment.setText(co.content);
        viewHolder.datetime.setText(convertTime(co.date));
        viewHolder.expandBtn.setText("답글보기");

        viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else ((ExpandableListView) parent).expandGroup(groupPosition, true);
            }
        });
        viewHolder.add_recomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment co = parentData.get(groupPosition);
                String comment_writer = co.comment_writer;
              //  mlistener.onRecomment(v, comment_writer, parent_id(groupPosition), groupPosition);



            }
        });
        /*
        viewHolder.expandBtn.setText("답글보기(" + getChildrenCount(groupPosition)+")"); //setting

        if(name_comment.equals(user_id)){
            viewHolder.edit_btn.setVisibility(View.VISIBLE);
            edit_event(viewHolder.edit_btn, vo, parentData);
        }else{
            viewHolder.edit_btn.setVisibility(View.INVISIBLE);
        }




*/
        return convertView;


    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.diary_recomment_listview, null);
            viewHolder.name = convertView.findViewById(R.id.recomment_list_name);
            viewHolder.comment = convertView.findViewById(R.id.recomment_list_content);
            viewHolder.edit_btn=convertView.findViewById(R.id.recomment_list_delete);
            viewHolder.datetime=convertView.findViewById(R.id.recomment_list_datetime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
/*
        final Comment vo = childData.get(groupPosition).get(childPosition);
        String name_comment = vo.comment.get("name");
        viewHolder.name.setText(name_comment);
        viewHolder.comment.setText(vo.comment.get("comment"));
        viewHolder.datetime.setText(convertTime(vo.datetime));

        // 댓글 등록한 name과 본인 이름이 같은 경우 삭제 버튼 활성화
        if(name_comment.equals(user_id)){
            viewHolder.edit_btn.setVisibility(View.VISIBLE);
            edit_event(viewHolder.edit_btn, vo, childData.get(groupPosition));


        }else{
            viewHolder.edit_btn.setVisibility(View.INVISIBLE);
        }*/

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public String convertTime(long time){
        long diff = (System.currentTimeMillis() - time)/(1000*60);  //minute difference
        if(diff<60){
            return diff+"분 전";
        }else if(diff<60*12){
            return diff/60 + "시간 전";
        } else {
            Date date = new Date(time);
            Format format = new SimpleDateFormat("yyyy.MM.dd");
            return format.format(date);
        }
    }

}

class ViewHolder {
    public TextView name;
    public TextView comment;
    public TextView expandBtn;
    public TextView add_recomment;
    public TextView edit_btn;
    public TextView datetime;
}