package com.example.myapplication.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "informationActivity";
    private FirebaseUser user;
    private Button confirm;
    private EditText info_text;
    private EditText name_text;
    private DatabaseReference ref;
    private String user_info;
    private ImageView profile_image;
    private final int PICK_IMAGE = 1;
    private Uri imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        info_text=findViewById(R.id.info_edit);
        name_text = findViewById(R.id.info_name);
        confirm=findViewById(R.id.info_btn_confirm);
        profile_image=findViewById(R.id.information_pic);
        profile_image.setOnClickListener(this);

        ref = FirebaseDatabase.getInstance().getReference();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = name_text.getText().toString();
               // String user_uri = uri_text.getText().toString();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest
                        .Builder().setDisplayName(user_name)
                        .setPhotoUri(imagePath)
                        .build();

                user.updateProfile(profileChangeRequest)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "User profile updated");
                                }
                            }
                        });
                String user_info=info_text.getText().toString();
                addInfo(user_info);
                finish();
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String user_name = user.getDisplayName();
            imagePath = user.getPhotoUrl();
            ref.child("userInfo").child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                user_info = dataSnapshot.getValue(String.class);
                                info_text.setText(user_info);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            name_text.setText(user_name);
            if(imagePath!=null) setImage(imagePath);
        }
    }

    private void addInfo(String info) {
        ref.child("userInfo").child(user.getUid()).setValue(info);
    }


    @Override
    public void onClick(View v) {
        if(v==profile_image){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setImage(Uri imagePath) {
        try {
        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        profile_image.setImageBitmap(bitmap);
        }catch (Exception e){
            Toast.makeText(this, "error: "+e , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            imagePath = data.getData();
            setImage(imagePath);

        }
    }
}
