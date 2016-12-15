package com.danxx.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import danxx.library.widget.ThreeDViewContainer;

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

//        ShadowViewHelper.bindShadowHelper(
//                new ShadowProperty()
//                        .setShadowColor(0x77000000)
//                        .setShadowDy(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 1.5f))
//                        .setShadowRadius(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 8))
//                , findViewById(R.id.view1));
//        ShadowViewHelper.bindShadowHelper(
//                new ShadowProperty()
//                        .setShadowColor(0x77000000)
//                        .setShadowDy(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 1.5f))
//                        .setShadowRadius(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 8))
//                , findViewById(R.id.view2));
//
//        ShadowViewHelper.bindShadowHelper(
//                new ShadowProperty()
//                        .setShadowColor(0x77000000)
//                        .setShadowDy(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 1.5f))
//                        .setShadowRadius(DeviceUtils.dp2px(ActivityCustomImgContainer.this, 8))
//                , findViewById(R.id.view3));


        ThreeDViewContainer threeDViewContainer = (ThreeDViewContainer) findViewById(R.id.threeDViewContainer);
        threeDViewContainer.requestLayout();
    }


}
