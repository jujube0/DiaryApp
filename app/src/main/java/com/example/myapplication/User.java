package com.example.myapplication;

import android.renderscript.Sampler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    private DatabaseReference mDatabase;
    static int user_num = 0;

    public User(){}

    public User(String username, String email){
        this.username = username;
        this.email=email;
    }
    public boolean CreateAccount(User user){
        boolean isAdded = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").push().setValue(user);
        user_num++;
        return true;
    }
/*

    public boolean checkDup(final String email){
        final boolean check = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return check;
    }
*/

}
