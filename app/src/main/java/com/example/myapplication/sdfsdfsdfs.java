package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.myapplication.Main.LongParaMenu;
import com.example.myapplication.Main.MainActivity;
import com.example.myapplication.Main.ShortParaMenu;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class sdfsdfsdfs extends FragmentActivity {

    static final int NUM_ITEMS = 2;
    ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdfsdfsdfs);

        viewPager = findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
/*

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
*/


        ;


    }

       class PagerAdapter extends FragmentPagerAdapter {
           private List<Fragment> fragments = new ArrayList<>();
           private List<String> titles = new ArrayList<>();

           public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
               super(fm, behavior);
               fragments.add(new ShortParaMenu());
               fragments.add(new LongParaMenu());
               titles.add("short");
               titles.add("long");
           }

           @NonNull
           @Override
           public Fragment getItem(int position) {
               return fragments.get(position);
           }

           @Override
           public int getCount() {
               return NUM_ITEMS;
           }

           @Nullable
           @Override
           public CharSequence getPageTitle(int position) {
               return titles.get(position);
           }
           @Override
           public int getItemPosition(Object object) {
               return POSITION_NONE;
           }
       }
    }


