package com.ndk;

/**
 * Created by Dawish on 2017/2/4.
 */

public class NdkJniUtils {
    static {
        System.loadLibrary("DanxxJniLibName");   //defaultConfig.ndk.moduleName
    }
    public native String getCLanguageString();

}
