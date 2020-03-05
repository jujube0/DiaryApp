package com.example.myapplication.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Main.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText getPassword;
    private EditText getEmail;
    private EditText getName;
    private Button backToLogin;
    private Button createAccount;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getEmail = findViewById(R.id.cre_getEmail);
        getPassword = findViewById(R.id.cre_getPassword);
        getName = findViewById(R.id.cre_getName);
        backToLogin = findViewById(R.id.cre_btn_login);
        backToLogin.setOnClickListener(this);
        createAccount = findViewById(R.id.cre_btn_create);
        createAccount.setOnClickListener(this);
        progressBar=findViewById(R.id.cre_progressBar);

        mAuth=FirebaseAuth.getInstance();
    }
    //사용자가 로그인되어있는 지 확인하는 함수
    @Override
    public void onStart(){
        super.onStart();
        //사용자가 로그인되어있는지 확인하고 UI를 업데이트
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            sendToMain();
        }

    }
    @Override
    public void onClick(View v) {
        if(v == backToLogin){
            finish();
        }else if(v == createAccount){

            String email = getEmail.getText().toString();
            String password = getPassword.getText().toString();
            final String name = getName.getText().toString();
            if(checkEmail(email)&&checkPass(password)&& !TextUtils.isEmpty(name)) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();
                                    user.updateProfile(profileUpdates);
                                    sendToMain();
                                } else {
                                    //if sign in fails, display a message to the user
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(CreateAccount.this, "Error: "+errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                progressBar.setVisibility(View.INVISIBLE);
            }else{
                if(!checkEmail(email)){
                    Toast.makeText(CreateAccount.this, "email이 올바르지 않습니다", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CreateAccount.this, "비밀번호는 8자 이상 16자 이하의 소문자와 숫자로 이루어져야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void sendToMain(){
        Intent mainIntent = new Intent(CreateAccount.this, MainActivity.class);
        startActivity(mainIntent);
    }
    // check if email is valid
    private boolean checkEmail(String email){
        if(email.contains("@")) return true;
        else return false;
    }
    // 비밀번호가 소문자와 숫자를 포함한, 8자 이상, 16자 이하인지 확인
    private boolean checkPass(String pass){
        boolean check;
        if(pass.matches("^[a-z0-9]{8,16}$")) return true;
        else return false;
    }

}
