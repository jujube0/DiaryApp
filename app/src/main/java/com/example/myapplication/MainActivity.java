package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView mainImageView;
    TextView visitorView;
    String visitior_today;
    String visitor_total;
    TextView main_textView_name;
    TextView main_btn_blog;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainImageView=findViewById(R.id.main_pic_parent_view);
        mainImageView.setOnClickListener(this);
        main_textView_name=findViewById(R.id.main_name_title_view);
        main_textView_name.setOnClickListener(this);
        main_btn_blog=findViewById(R.id.main_btn_blog);
        main_btn_blog.setOnClickListener(this);
        visitorView=findViewById(R.id.main_visitor_text_view);

        visitor_total=getResources().getString(R.string.visitor_total);
        visitior_today=getResources().getString(R.string.visitor_today);
        visitorView.setText("오늘 "+visitior_today+"  전체"+visitor_total);

       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG,  "Value is: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", error.toExeption());
            }
        });*/

       mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        if(v==mainImageView||v==main_textView_name){
            Intent intent = new Intent(this, InformationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else if(v==main_btn_blog){
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
        }

    }
}
