package com.jzp.myapplication;

import android.util.Log;

/**
 * Created by Dawish on 2017/2/11.
 */

public class JniUtils {

    public static native String getStringC();

    public static native String myEncrypt(String content);

    public static native String myDecrypt(String content);

    static {
        System.loadLibrary("DanxxJniLibName");//之前在build.gradle里面设置的so名字，必须一致
    }

    public static String encrypt(String key, String plainText) throws  Exception{
        //TODO 加密
        Log.i("danxx", "加密--->"+plainText);
        return "这是加密前的字符串";
    }

    public static String decrypt(String key, String cryptedText) throws  Exception{
        //TODO 解密
        Log.i("danxx", "解密--->"+cryptedText);
        return "这是解密后的字符串";
    }
}
