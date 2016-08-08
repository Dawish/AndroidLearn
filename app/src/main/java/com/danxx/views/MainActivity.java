package com.danxx.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
