package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import danxx.library.widget.StackCardContainer;

/**
 * @Description:
 * @Author: Danxingxi
 * @CreateDate: 2016/12/13 19:51
 */
public class ActivityCustomImgContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_three_d);

        StackCardContainer threeDViewContainer = (StackCardContainer) findViewById(R.id.threeDViewContainer);
        threeDViewContainer.setOnItemViewClickListener(new StackCardContainer.OnItemViewClickListener() {
            @Override
            public void onItemViewOnClickListener(View itemView, int position) {
                Toast.makeText(ActivityCustomImgContainer.this, "Position->"+position, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
