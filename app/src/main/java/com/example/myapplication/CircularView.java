package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircularView extends CircleImageView {
    public CircularView(Context context) {
        super(context);
    }

    public CircularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
