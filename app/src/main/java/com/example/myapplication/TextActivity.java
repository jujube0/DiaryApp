package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {

    Button text_add_btn;
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

        }
    }
}
