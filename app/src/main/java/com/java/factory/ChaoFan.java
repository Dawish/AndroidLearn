package com.java.factory;

import android.util.Log;

/**
 * 炒饭接口，做所有炒饭需要的步骤
 * Created by Dawish on 2016/10/16.
 */

public abstract class ChaoFan {

    /**
     * 什么炒饭
     */
    String name;

    /**
     * 顾客的信息
     */
    String userData;

    /**
     * 做炒饭之前的准备
     */
    void prepare(){
        Log.d( name, name+" prepare...");
    }

    /**
     * 开始炒饭
     */
    void fry(){
        Log.d( name, name+" fry");
    }

    /**
     * 打包
     */
    void box(){
        Log.d( name, name+" box");
        Log.d( name, name+" 打包贴上顾客信息:"+userData);
    }

    public String getUserData(){
        return userData;
    }


}
