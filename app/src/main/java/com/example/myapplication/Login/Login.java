package com.example.myapplication.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {


    private EditText getEmail;
    private EditText getPass;
    private Button create_btn;
    private Button login_btn;
    private TextView textView;
    private FirebaseAuth mAuth;

    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        getEmail=findViewById(R.id.login_getEmail);
        getPass = findViewById(R.id.login_getPassword);

        create_btn=findViewById(R.id.login_create_btn);
        login_btn=findViewById(R.id.login_btn);
        create_btn.setOnClickListener(this);
        loginProgress=(ProgressBar) findViewById(R.id.login_progressBar);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = getEmail.getText().toString();
                String pass = getPass.getText().toString();
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendToMain();
                            }else{
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(Login.this, "Error: "+errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    loginProgress.setVisibility(View.INVISIBLE);

                }
            }
        });
        //initializing

    }
    @Override
    public void onStart(){
        super.onStart();

        //check if user is signed in and update ui accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null){
            sendToMain();
        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    @Override
    public void onClick(View v) {
        if(v == create_btn){
            Intent CreIntent = new Intent(this, CreateAccount.class);
            startActivity(CreIntent);
            //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }
    void showToast(String message){
        Toast t = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        t.show();
    }
}
