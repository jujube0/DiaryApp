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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CommentAdapter extends BaseExpandableListAdapter {
    Context context;
    String user_name;

    ArrayList<DriveComment> parentData = new ArrayList<>();
    ArrayList<ArrayList<DriveComment>> childData = new ArrayList<>();
    private OnRecommentListener mListener;

    public interface OnRecommentListener {
        void onRecomment(View v, String name, int parent_id, int group_pos);
    }




    public CommentAdapter(Context context, OnRecommentListener mlistener, String user_name) {
        super();
        this.context = context;
        this.user_name = user_name;

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = db.rawQuery("select _id, name, comment from tb_comment where parent is null and deleted = 0 order by _id", null);
            while (cursor.moveToNext()) {

                DriveComment vo = new DriveComment();
                HashMap<String, String> data = new HashMap<>();
                data.put("name", cursor.getString(1));
                data.put("comment", cursor.getString(2));
                vo.comment = data;
                vo.id = cursor.getInt(0);
                //vo.datetime=cursor.getString(3);
                parentData.add(vo);
                //child view
                String c = "select _id, name, comment from tb_comment where deleted = 0 and parent = "+vo.id + " order by _id";
                Cursor cursor_child = db.rawQuery(c, null);
                ArrayList<DriveComment> child = new ArrayList<>();
                while (cursor_child.moveToNext()) {
                    DriveComment vo_child = new DriveComment();
                    HashMap<String, String> data_child = new HashMap<>();
                    vo_child.id = cursor_child.getInt(0);
                    data_child.put("name", cursor_child.getString(1));
                    data_child.put("comment", cursor_child.getString(2));
                    vo_child.comment = data_child;

                    child.add(vo_child);
                }
                childData.add(child);
            }

            mListener = mlistener;

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
    public DriveComment getGroup(int groupPosition) {
        return parentData.get(groupPosition);
    }

    @Override // get information of player
    public DriveComment getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        DriveComment vo = (DriveComment)getGroup(groupPosition);
        return vo.id;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        DriveComment vo = (DriveComment)getChild(groupPosition, childPosition);
        return vo.id;
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

        final DriveComment vo = getGroup(groupPosition);
        String name_comment = vo.comment.get("name");
        viewHolder.name.setText(name_comment);
        viewHolder.comment.setText(vo.comment.get("comment"));
        viewHolder.datetime.setText(""+vo.id);
        viewHolder.expandBtn.setText("답글보기(" + getChildrenCount(groupPosition)+")"); //setting

        if(name_comment.equals(user_name)){
            viewHolder.edit_btn.setVisibility(View.VISIBLE);
            edit_event(viewHolder.edit_btn, true, vo, parentData);
        }else{
            viewHolder.edit_btn.setVisibility(View.INVISIBLE);
        }


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

                final DriveComment vo = parentData.get(groupPosition);
                String name = vo.comment.get("name");
                mListener.onRecomment(v, name, vo.id, groupPosition);


            }
        });

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

        final DriveComment vo = childData.get(groupPosition).get(childPosition);
        String name_comment = vo.comment.get("name");
        viewHolder.name.setText(name_comment);
        viewHolder.comment.setText(vo.comment.get("comment"));
        viewHolder.datetime.setText(""+vo.id);

        // 댓글 등록한 name과 본인 이름이 같은 경우 삭제 버튼 활성화
        if(name_comment.equals(user_name)){
            viewHolder.edit_btn.setVisibility(View.VISIBLE);
            edit_event(viewHolder.edit_btn, false, vo, childData.get(groupPosition));


        }else{
            viewHolder.edit_btn.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    void edit_event(TextView view, final boolean isParent, final DriveComment vo, final ArrayList<DriveComment> list){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("해당 댓글을 삭제하시겠습니까?");
                builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBHelper helper = new DBHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();

                        list.remove(vo);
                        db.execSQL("update tb_comment Set deleted = 1 where _id= "+vo.id);
                        notifyDataSetChanged();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });
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