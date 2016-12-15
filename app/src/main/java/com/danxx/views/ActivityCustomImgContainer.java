package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        threeDViewContainer.requestLayout();
    }


}
