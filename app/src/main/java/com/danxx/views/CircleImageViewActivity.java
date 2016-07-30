package com.danxx.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import danxx.library.widget.SampleCircleImageView;

/**
 * Created by Danxx on 2016/7/29.
 */
public class CircleImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("danxx" ,"CircleImageViewActivity");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vp1);
        SampleCircleImageView circleImageView = new SampleCircleImageView(CircleImageViewActivity.this, bitmap);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
//        setContentView(circleImageView ,params);
        setContentView(R.layout.activity_sample_circle_imageview);
    }
}
