package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

public class CommentActivity extends AppCompatActivity {

    ExpandableListView comment_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        comment_listView = findViewById(R.id.diary_comment_content_list);


        ArrayList<DriveComment> parentData = new ArrayList<>();
        ArrayList<ArrayList<DriveComment>> childData = new ArrayList<>();

        DriveComment vo = new DriveComment();

        HashMap<String, String> data = new HashMap<>();
        vo = new DriveComment();
        data.put("name", "사용자1");
        data.put("comment", "안녕하세요");
        vo.comment = data;
        parentData.add(vo);

        data = new HashMap<>();
        vo = new DriveComment();
        data.put("name", "사용자2");
        data.put("comment", getString(R.string.text));
        vo.comment = data;
        parentData.add(vo);

        ArrayList<DriveComment> parentComment = new ArrayList<>();
        vo = new DriveComment();
        data = new HashMap<>();
        data.put("name", "사용자3");
        data.put("comment", "사용자 1의 댓글에 대한 대댓글입니다.");
        vo.comment = data;
        parentComment.add(vo);

        data = new HashMap<>();
        vo = new DriveComment();
        data.put("name", "사용자4");
        data.put("comment", "사용자 1의 댓글에 대한 대댓글2입니다.");
        vo.comment = data;
        parentComment.add(vo);
        childData.add(parentComment);


        data = new HashMap<>();
        vo = new DriveComment();
        parentComment = new ArrayList<>();
        data.put("name", "사용자6");
        data.put("comment", "사용자 2의 댓글에 대한 대댓글2입니다.");
        vo.comment = data;
        parentComment.add(vo);
        childData.add(parentComment);


        CommentAdapter adapter = new CommentAdapter(this, parentData, childData);
        comment_listView.setAdapter(adapter);



        comment_listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });;


    }

    public class CommentAdapter extends BaseExpandableListAdapter {
        Context context;

        ArrayList<DriveComment> parentData = new ArrayList<>();
        ArrayList<ArrayList<DriveComment>> childData = new ArrayList<>();

        public CommentAdapter(Context context, ArrayList<DriveComment> parentData, ArrayList<ArrayList<DriveComment>> childData) {
            super();
            this.context = context;
            this.parentData = parentData;
            this.childData = childData;
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
        public Object getGroup(int groupPosition) {
            return parentData.get(groupPosition);
        }

        @Override // get information of player
        public Object getChild(int groupPosition, int childPosition) {
            return childData.get(groupPosition);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.diary_comment_listview, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_list_name);
                viewHolder.comment = convertView.findViewById(R.id.comment_list_content);
                viewHolder.expandBtn=convertView.findViewById(R.id.comment_list_show_recomment);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final DriveComment vo = parentData.get(groupPosition);
            viewHolder.name.setText(vo.comment.get("name"));
            viewHolder.comment.setText(vo.comment.get("comment"));
            viewHolder.expandBtn.setText("답글보기(" + getChildrenCount(groupPosition)+")");

            viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                    else ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            });


            return convertView;


        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.diary_recomment_listview, null);
                viewHolder.name = convertView.findViewById(R.id.recomment_list_name);
                viewHolder.comment = convertView.findViewById(R.id.recomment_list_content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final DriveComment vo = childData.get(groupPosition).get(childPosition);
            viewHolder.name.setText(vo.comment.get("name"));
            viewHolder.comment.setText(vo.comment.get("comment"));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }



    }

    class ViewHolder {
        public TextView name;
        public TextView comment;
        public TextView expandBtn;
    }
    public void ShowToast(String message){
        Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        t.show();
    }

}
