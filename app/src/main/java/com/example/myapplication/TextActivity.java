package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {

    Button text_add_btn;
    EditText title_view;
    EditText content_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);

        text_add_btn=findViewById(R.id.addbtn);
        text_add_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==text_add_btn){
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            title_view = findViewById(R.id.text_title);
            content_view = findViewById(R.id.text_content);

            String title = title_view.getText().toString();
            String content = content_view.getText().toString();

            db.execSQL("insert into tb_comment (name, comment) values (?,?)",
                    new String[]{title, content});
            db.close();

            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);

        }
    }
}
