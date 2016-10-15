package com.java.factory;

import android.util.Log;

/**
 * Created by Dawish on 2016/10/16.
 */

public class RoseChaoFan extends ChaoFan {

    private final static String TAG = "RoseChaoFan";

    /**
     *
     * @param mUserData 在做炒饭的时候把用户信息告诉厨房，方便在打包的时候贴上用户信息
     */
    public RoseChaoFan(String mUserData){
        name = TAG;
        userData = mUserData;
        addRose();
    }

    public void addRose(){
        Log.d(TAG, "加肉丝>>>");
    }

    /**
     * 是否加辣椒
     */
    public ChaoFan addChilli(){
        Log.d(TAG, "肉丝炒饭加辣椒>>>");
        return this;
    }

}
