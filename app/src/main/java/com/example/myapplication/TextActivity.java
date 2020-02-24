package com.example.myapplication;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TextActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    Button text_addBtn;
    Button img_addBtn;
    EditText title_view;
    EditText content_view;
    TextView set_category;
    String category;
    LinearLayout layout;
    public static final int PICK_IMAGE = -1;
    String mCurrentPhotoPath;
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        set_category = findViewById(R.id.text_setCategory);



        img_addBtn = findViewById(R.id.textActivity_btn_addPhoto);
        img_addBtn.setOnClickListener(this);
        text_addBtn=findViewById(R.id.addbtn);
        text_addBtn.setOnClickListener(this);
//        layout = (LinearLayout)layout.findViewById(R.id.text_linearLayout);
  //      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1f);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public void onClick(View view) {
        if(view==text_addBtn){
            title_view = findViewById(R.id.text_title);
            content_view = findViewById(R.id.text_content);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            String title = title_view.getText().toString();
            String content = content_view.getText().toString();
            BlogPost post = new BlogPost("김가영", title, content,category, System.currentTimeMillis());
            mDatabase.child("blogPosts").push().setValue(post);
        }else if(view == img_addBtn){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    }
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.diary_category_menu, popup.getMenu());
        popup.show();
    }

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
        switch(requestCode){
            case PICK_IMAGE:
                if(data.getData()!=null){

                    try{
                        filePath = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                        ref.putFile(filePath);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;


        }
    }
    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis()+".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

}
