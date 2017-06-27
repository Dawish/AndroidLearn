package com;

import android.app.Application;

import com.dximageloader.DxImageLoader;

/**
 * Created by Administrator on 2017/6/27.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化图片加载库
        DxImageLoader.getInstance().init(getApplicationContext());
    }
}
