package com.java.factory;

import android.util.Log;

/**
 * 鸡蛋炒饭
 * Created by Dawish on 2016/10/16.
 */

public class EggChaoFan extends ChaoFan {

    private final static String TAG = "EggChaoFan";

    /**
     *
     * @param mUserData 在做炒饭的时候把用户信息高速厨房，方便在打包的时候贴上用户信息
     */
    public EggChaoFan(String mUserData){
        name = TAG;
        userData = mUserData;
        addEgg();
    }

    public void addEgg(){
        Log.d(TAG, "加鸡蛋>>>");
    }

    /**
     * 是否加辣椒
     */
    public ChaoFan addChilli(){
        Log.d(TAG, "鸡蛋炒饭加辣椒>>>");
        return this;
    }

}
