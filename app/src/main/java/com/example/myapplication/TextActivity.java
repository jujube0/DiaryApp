package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.UUID;

public class TextActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    Button text_addBtn;
    Button img_addBtn;
    EditText title_view;
    EditText content_view;
    TextView set_category;
    String category;
    //새로운 image가 들어있는 imageView를 추가할 layout
    LinearLayout layout;
    LinearLayout.LayoutParams layoutParams;

    int content_num = 1;



    public static final int PICK_IMAGE = 1;
    // image저장 path
    private Uri imagePath;
    StorageReference storageReference;
    FirebaseStorage storage;
    Queue<String> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        set_category = findViewById(R.id.text_setCategory);


        img_addBtn = findViewById(R.id.textActivity_btn_addPhoto);
        img_addBtn.setOnClickListener(this);
        text_addBtn=findViewById(R.id.addbtn);
        text_addBtn.setOnClickListener(this);
        // 새로운 image와 editText가 추가될 layout
        layout = (LinearLayout)findViewById(R.id.text_linearLayout);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public void onClick(View view) {
        //작성 완료 버튼
        if(view==text_addBtn){
            title_view = findViewById(R.id.text_title);
            content_view = findViewById(R.id.text_content_01);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            String title = title_view.getText().toString();
            String content = content_view.getText().toString();
            
            BlogPost post = new BlogPost("김가영", title,category, System.currentTimeMillis());
            mDatabase.child("blogPosts").push().setValue(post);
            //이미지 추가 버튼
        }else if(view == img_addBtn){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }
    //category popup Onclick
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.diary_category_menu, popup.getMenu());
        popup.show();
    }

    //카테고리 선택시 카테고리 창에 선택된 카테고리 이름 표시
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.basic_category:
                category = "기본메뉴";
                set_category.setText(category);
                return true;
            case R.id.category1:
                category = "category1";
                set_category.setText(category);
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == PICK_IMAGE){

          imagePath = data.getData();
          try{
              Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
              CreateImageView(bitmap);
              StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
              ref.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      Toast.makeText(TextActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(TextActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                  }
              });
          }
          catch (IOException e){
              e.printStackTrace();
          }
        }
    }
    // bitmap을 받아 imageview를 layout에 추가하고, 글을 입력할 수 있는 editText도 함께 추가.
    void CreateImageView(Bitmap bitmap){
        ImageView imageView = new ImageView(this);
        EditText editText = new EditText(this);

        imageView.setImageBitmap(bitmap);
        layout.addView(imageView, layoutParams);
        layout.addView(editText, layoutParams);
        // imageView 밑에 EditText추가 후 새로운 EditText에 focus 부여
        editText.requestFocus();
    }

}
