package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText getEmail;
    EditText getName;
    Button create_btn;
    Button login_btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getEmail = findViewById(R.id.login_getId);
        getName = findViewById(R.id.login_getname);
        create_btn = findViewById(R.id.login_create_btn);
        create_btn.setOnClickListener(this);
        login_btn=findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        textView = findViewById(R.id.login_text);



    }
    @Override
    public void onClick(View v) {
        if(v == create_btn){
            String email = getEmail.getText().toString();
            String name = getName.getText().toString();
            User user = new User(name, email);
            user.CreateAccount(user);

            getEmail.setText(null);
            getName.setText(null);
            showToast("계정이 생성 되었습니다.");
        }else if(v == login_btn){


        }
    }
    void showToast(String message){
        Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        t.show();
    }
}
