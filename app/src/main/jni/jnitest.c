#include "com_ndk_NdkJniUtils.h"

/*
 * Class:     com_ndk_NdkJniUtils
 * Method:    getCLanguageString
 * Signature: ()Ljava/lang/String;
 * JNIEnv *env 接口指针
 * object obj “this”指针
 * JNIEXPORT 和 JNICALL 是 jni 的宏，在 android 的 jni 中不需要，当然写上去也不会有错。
 */
JNIEXPORT jstring JNICALL Java_com_ndk_NdkJniUtils_getCLanguageString(JNIEnv *env, jobject obj)
{
    return (*env)->NewStringUTF(env,"This just a test for Android Studio NDK JNI developer!");
}