package com.example.myapplication.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
// 사진 크기 맞춰야됨 시부럴
//삭제 버튼 눌렀을 때 storage에서도 삭제하도록

public class Add_long_para extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    Button text_addBtn;
    Button img_addBtn;
    EditText title_view;
    TextView set_category;

    private FirebaseDatabase firebaseDatabase;
    String category;
    //새로운 image가 들어있는 imageButton를 추가할 layout
    LinearLayout layout;
    LinearLayout.LayoutParams layoutParams;

// 0은 title, 1은 첫번째 editText
    int content_num = 2;
    HashMap<Integer, BlogPost> contents;

    LinearLayout.LayoutParams imageParams;

    public static final int PICK_IMAGE = 1;
    // image저장 path
 //  private String imagePath;

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
        // 새로운 image와 editText가 추가될 layout
        layout = (LinearLayout)findViewById(R.id.text_linearLayout);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        contents = new HashMap<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        imageParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, toDp(350)
        );
    }

    @Override
    public void onClick(View view) {
        //작성 완료 버튼
        if(view==text_addBtn){
            title_view = findViewById(R.id.text_title);
            //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("blogPosts");
            String title = title_view.getText().toString();
            //BlogPost post = new BlogPost("김가영", title, category, System.currentTimeMillis());
           // mDatabase.child("blogPosts").push().setValue(post); // title 형성

            Map<String, BlogPost> posts = new HashMap<>();
            //제목 넣기. 제목이 없으면 제목 설정하라고 toast
            if(!title.equals("")) {
                BlogPost mainPost = new BlogPost("김가영", title, category, System.currentTimeMillis());
                //posts.put("0", new BlogPost("김가영", title, category, new Timestamp(System.currentTimeMillis())));
                // 첫번째 editText의 id 변경
                EditText first_view = findViewById(R.id.text_content_01);
                if (!(first_view.getText().toString().equals(""))) {
                    String t = first_view.getText().toString();
                    posts.put("1", new BlogPost(1, BlogPost.TEXT, t));
                    mainPost.content = t;
                }

                for (int i = 2; i < content_num; i++) {
                    // content를 담고있는 imageButton와 EditText를 순서대로 가져와 넣기
                    if (!(contents.get(i) == null)) {
                        BlogPost p = contents.get(i);
                        if (p.type == BlogPost.IMAGE) {
                            if(mainPost.img_url == null) mainPost.img_url = p.img_url;
                            posts.put("" + i, p);
                        } else {
                            EditText editText = findViewById(i);
                            if (!(editText.getText().toString().equals(""))) {
                                String text = editText.getText().toString();
                                if(mainPost.content ==null) mainPost.content = text;
                                p.putContent(text);
                                posts.put("" + i, p);
                            }
                        }
                    }
                }
                posts.put("0", mainPost);
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("blogPosts").child(user_id);
                mDatabase.push().setValue(posts);

                // 완료되면 완료 Toast.
                Toast.makeText(this, "작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
            }

            //이미지 추가 버튼
        }else if(view == img_addBtn){
/*            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, PICK_IMAGE);
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
    private void addImageView(String imgPath) {
        final ImageButton imageButton = new ImageButton(this);
        imageParams.setMargins(0,toDp(10),0,toDp(10));
        Glide.with(this).load(imgPath).into(imageButton);
        layout.addView(imageButton, imageParams);
        imageButton.setId(content_num);
        contents.put(content_num, new BlogPost(content_num, BlogPost.IMAGE, imgPath));
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "이미지를 삭제하시겠습니까?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageButton.setVisibility(GONE);
                        contents.remove(imageButton.getId());
                    }
                }).show();
                return false;
            }
        });
        content_num++;
        final EditText editText = new EditText(this);
        // backspace를 만나면 없어지기
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL&&(content_num-1)!=editText.getId()){
                    editText.setVisibility(GONE);
                }
                return false;
            }
        });
        editText.setId(content_num);
        contents.put(content_num, new BlogPost(content_num, BlogPost.TEXT));
        content_num++;

        layout.addView(editText, layoutParams);
        editText.setBackgroundResource(android.R.color.transparent);
        editText.requestFocus();
        editText.setHint("내용을 입력하세요");

    }
    private int toDp(int value){
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());

    }

    //bitmap은 시간이 너무 오래걸려서 없앴음
 /*   // bitmap을 받아 imageButton를 layout에 추가하고, 글을 입력할 수 있는 editText도 함께 추가.
    void CreateimageButton(Bitmap bitmap, String imgPath){
        final ImageButton imageButton = new ImageButton(this);
        imageButton.setId(content_num);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contents.put(content_num, new BlogPost(content_num, BlogPost.IMAGE, imgPath));
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "이미지를 삭제하시겠습니까?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageButton.setVisibility(GONE);
                        contents.remove(imageButton.getId());
                    }
                }).show();
                return false;
            }
        });
        content_num++;
        final EditText editText = new EditText(this);
        // backspace를 만나면 없어지기
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL&&(content_num-1)!=editText.getId()){
                    editText.setVisibility(GONE);
                }
                return false;
            }
        });
        editText.setId(content_num);
        contents.put(content_num, new BlogPost(content_num, BlogPost.TEXT));
        content_num++;

        layout.addView(imageButton, layoutParams);
        layout.addView(editText, layoutParams);
        imageButton.setImageBitmap(bitmap);
        //File file = new File(imagePath);
        //Glide.with(this).load(file).into(imageButton);
        // imageButton 밑에 EditText추가 후 새로운 EditText에 focus 부여
        editText.setBackgroundResource(android.R.color.transparent);
        editText.requestFocus();
        editText.setHint("내용을 입력하세요");
    }*/
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
// 데이터베이스 이미지 추가할떄
    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE){
            //imagePath = getPath(data.getData());
// final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            StorageReference storageReference = storage.getReferenceFromUrl("gs://myapplication-f3f26.appspot.com");

            //upload
            final StorageReference riversRef = storageReference.child("blogPosts/" + data.getData().getLastPathSegment());

            riversRef.putFile(data.getData()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Add_long_para.this,"이미지를 가져오는 데에 실패하였습니다",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();

                    //CreateimageButton(bitmap, url.toString());
                    addImageView(url.toString());
                    Toast.makeText(Add_long_para.this,"이미지를 길게 누르면 삭제됩니다",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
