package com.danxx.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.aopBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("danxx", "onClick--->");
            }
        });

    }

    public void CustomViewPagerActivity(View v){
        Intent intent = new Intent(MainActivity.this,CustomViewPagerActivity.class);
        startActivity(intent);
    }

    public void CircleImageViewActivity(View view){
        Intent intent = new Intent(MainActivity.this,CircleImageViewActivity.class);
        startActivity(intent);
    }

    public void ActivityVerticalTabIndicator(View view){
        Intent intent = new Intent(MainActivity.this,ActivityVerticalTabIndicator.class);
        startActivity(intent);
    }

    public void RefreshScrollActivity(View view){
        Intent intent = new Intent(MainActivity.this,RefreshScrollActivity.class);
        startActivity(intent);
    }

    public void RefreshListViewActivity(View view){
        Intent intent = new Intent(MainActivity.this,RefreshListViewActivity.class);
        startActivity(intent);
    }

    public void ImageLoadActivity(View view){
        Intent intent = new Intent(MainActivity.this,ImageLoadActivity.class);
        startActivity(intent);
    }

    public void scoket(View v){
        Intent intent = new Intent(MainActivity.this, SocketActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
