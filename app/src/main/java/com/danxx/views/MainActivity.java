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
}
