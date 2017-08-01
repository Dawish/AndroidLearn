package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import danxx.library.widget.InvalidateTest;
import danxx.library.widget.OnePlusCloudy;
import danxx.library.widget.StackCardContainer;

/**
 * Created by Dawish on 2016/12/27.
 */

public class ActivityTest extends AppCompatActivity {
    OnePlusCloudy onePlusCloudy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_invalidate);
        onePlusCloudy = (OnePlusCloudy) findViewById(R.id.onePlusCloudy);
    }
    public void start(View view){
        onePlusCloudy.start();
    }
    public void stop(View view){
        onePlusCloudy.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
