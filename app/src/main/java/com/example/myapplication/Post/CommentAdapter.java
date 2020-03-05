package com.example.myapplication.Post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
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
import java.util.HashMap;

public class CommentAdapter extends BaseExpandableListAdapter {
    Context context;
    String user_id;

    ArrayList<Comment> parentData;
    HashMap<Integer, ArrayList<Comment>> childData;
    private OnRecommentListener mlistener;
    public interface OnRecommentListener {
        void onRecomment(View v, String name, int parent_id, int group_pos);
    }




    public CommentAdapter(Context context, OnRecommentListener mlistener, String user_id, ArrayList<Comment> parentData,HashMap<Integer, ArrayList<Comment>> childData) {
        super();
        this.context = context;
        this.user_id = user_id;
        this.mlistener=mlistener;
        this.parentData = parentData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return parentData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) { // childeren 수
        return childData.get((int)getGroupId(groupPosition)).size();
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

        return parentData.get(groupPosition).comment_id;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childData.get((int)getGroupId(groupPosition)).get(childPosition).comment_id;
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

        viewHolder.name.setText(co.author_account);
        viewHolder.comment.setText(co.content);
        viewHolder.datetime.setText(convertTime(co.date));
        viewHolder.expandBtn.setText("답글보기(" + getChildrenCount(groupPosition)+")"); //setting
        if(co.author_account.equals(user_id)){
            viewHolder.edit_btn.setVisibility(View.VISIBLE);
        }else{
            viewHolder.edit_btn.setVisibility(View.INVISIBLE);
        }

        viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else if(!(childData.get(co.comment_id)==null))((ExpandableListView) parent).expandGroup(groupPosition, true);
            }
        });
        viewHolder.add_recomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment co = parentData.get(groupPosition);
                String author_account = co.author_account;
                mlistener.onRecomment(v, author_account, parentData.get(groupPosition).comment_id, groupPosition);
            }
        });
        //edit_btn에 click listener 등록하여 선택된 데이터를 삭제한다.
        //deleteComment(viewHolder.edit_btn, co.comment_id);
        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("정말 삭제하시겠습니까?");;

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query = ref.child("comments").orderByChild("comment_id").equalTo(co.comment_id);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });;
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
        if(!(parentData.get(groupPosition)==null)){
            final Comment co = getChild(groupPosition, childPosition);
            viewHolder.name.setText(co.author_account);
            viewHolder.comment.setText(co.content);
            viewHolder.datetime.setText(convertTime(co.date));
            deleteComment(viewHolder.edit_btn, co.comment_id);

            // 댓글 등록한 name과 본인 이름이 같은 경우 삭제 버튼 활성화
            if(co.author_account.equals(user_id)){
                viewHolder.edit_btn.setVisibility(View.VISIBLE);
            }else{
                viewHolder.edit_btn.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    void deleteComment(TextView v, final int comment_id){

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("정말 삭제하시겠습니까?");;

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query query = ref.child("comments").orderByChild("comment_id").equalTo(comment_id);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                dataSnapshot.getRef().removeValue();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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