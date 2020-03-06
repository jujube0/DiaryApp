package com.example.myapplication.shortPost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Post.TextActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

public class Add_short_para extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private ImageView imageView;
    private Button btn_comfirm;
    private Button btn_addImage;
    final private int PICK_IMAGE = 1;
    // 사진저장
    private Uri imagePath;
    private Uri url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_short_para);
        editText=findViewById(R.id.add_short_content);
        imageView=findViewById(R.id.add_short_image);
        btn_comfirm=findViewById(R.id.add_short_confirm);
        btn_comfirm.setOnClickListener(this);
        btn_addImage=findViewById(R.id.add_short_add_photo);
        btn_addImage.setOnClickListener(this);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_addImage){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_IMAGE);
        }else if(v==btn_comfirm){
            String content = editText.getText().toString();
            ShortPost post;
            if(!(imagePath==null)){
                String image_url = UploadImage(imagePath);
                post = new ShortPost(System.currentTimeMillis(), image_url, content);
            }else{
                post = new ShortPost(System.currentTimeMillis(), content);
            }
            addPost(post);
            finish();
        }
    }

    private void addPost(ShortPost post) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("shortPosts");
        mDatabase.push().setValue(post);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            imagePath = data.getData();

            try{
                //image 추가하고 imagePath를 imageview에 tag
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
// 사진 추가
    private String UploadImage(Uri imagePath){
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://myapplication-f3f26.appspot.com");
      //  final StorageReference riversRef = storageReference.child("shortPosts/"+UUID.randomUUID());
        final Uri file = Uri.fromFile(new File(getPath(imagePath)));
        final StorageReference riversRef = storageReference.child("shortPosts/" + file.getLastPathSegment());
        riversRef.putFile(imagePath).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_short_para.this,"test fail",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri.isComplete());
                url = uri.getResult();
//                        SetImage(bitmap, url.toString());
            }
        });
        if(url!=null){
            return url.toString();
        }else{
            return "";
        }
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
}
