package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import danxx.library.widget.InvalidateTest;
import danxx.library.widget.StackCardContainer;

/**
 * Created by Dawish on 2016/12/27.
 */

public class ActivityTest extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_invalidate);

         final InvalidateTest invalidateTest = (InvalidateTest) findViewById(R.id.invalidateTest);

         final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contentl);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.postInvalidate(invalidateTest.getLeft(),invalidateTest.getTop(),
                        invalidateTest.getRight(),invalidateTest.getBottom());
            }
        });


//        linearLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                linearLayout.requestLayout();
//                Log.d("danxx", "linearLayout-------->");
////                invalidateTest.postInvalidate(280,280,100,300);
//            }
//        },2000);



    }
}
