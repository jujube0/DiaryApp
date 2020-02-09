package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView commentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        commentBtn = findViewById(R.id.diary_image_comment);
        commentBtn.setOnClickListener(this);

        TextView titleView = findViewById(R.id.diary_text_title);
        TextView contentView = findViewById(R.id.diary_text_content);
        TextView clickView = findViewById(R.id.diary_top_text_clickNum);

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select _id, name, comment from tb_comment order by _id desc limit 1", null);

        while(cursor.moveToNext()){
            clickView.setText(cursor.getString(0));
            titleView.setText(cursor.getString(1));
            contentView.setText(cursor.getString(2));
        }
        db.close();
    }

    @Override
    public void onClick(View view) {
        if(view == commentBtn){
            Intent intent = new Intent(this, CommentActivity.class);
            startActivity(intent);
        }
    }
}

