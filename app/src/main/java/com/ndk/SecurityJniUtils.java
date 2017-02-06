package com.ndk;

/**
 * 加解密jni工具类
 * Created by Dawish on 2017/2/6.
 */

public class SecurityJniUtils {
    /**从ndk获取字符串**/
    public static native String getStringC();
    /**加密字符串**/
    public static native String myEncrypt(String content);
    /**解密字符串**/
    public static native String myDecrypt(String content);
}
