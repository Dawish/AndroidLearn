#include "com_jzp_myapplication_JniUtils.h"
#include <stdlib.h>
const char *DES_KEY = "12345678912345678912345678912345";
/*
* Class:     com_jzp_myapplication_JniUtils
* Method:    getString
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_com_jzp_myapplication_JniUtils_getStringC
        (JNIEnv *env, jobject obj){
    return (*env)->NewStringUTF(env,"这里是来自jni的string");
}
JNIEXPORT jstring JNICALL Java_com_jzp_myapplication_JniUtils_myEncrypt
        (JNIEnv *env, jclass jclass1, jstring jstr)
{
    if (jstr == NULL) {
        return NULL;
    }
    jstring key;
    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    AESencrypt = (*env)->FindClass(env, "com/jzp/myapplication/JniUtils");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = (*env)->GetStaticMethodID(env, AESencrypt, "encrypt",
                                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        (*env)->DeleteLocalRef(env, AESencrypt);
        return NULL;
    }
    key = (*env)->NewStringUTF(env, DES_KEY);
    result = (*env)->CallStaticObjectMethod(env, AESencrypt, mid, key, jstr);
    (*env)->DeleteLocalRef(env, AESencrypt);
    (*env)->DeleteLocalRef(env, key);
    return result;
//    return (*env)->NewStringUTF(env,"加密返回");
}

JNIEXPORT jstring JNICALL Java_com_jzp_myapplication_JniUtils_myDecrypt
        (JNIEnv *env, jclass jclass1, jstring jstr)
{
    if (jstr == NULL) {
        return NULL;
    }
    jstring key;
    jstring result;
    jclass AESencrypt;
    jmethodID mid;

    AESencrypt = (*env)->FindClass(env, "com/jzp/myapplication/JniUtils");
    if (NULL == AESencrypt) {
        return NULL;
    }
    mid = (*env)->GetStaticMethodID(env, AESencrypt, "decrypt",
                                    "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    if (NULL == mid) {
        (*env)->DeleteLocalRef(env, AESencrypt);
//        return NULL;
        return (*env)->NewStringUTF(env,"返回为空1");
    }
    key = (*env)->NewStringUTF(env, DES_KEY);
    result = (*env)->CallStaticObjectMethod(env, AESencrypt, mid, key, jstr);
    (*env)->DeleteLocalRef(env, AESencrypt);
    (*env)->DeleteLocalRef(env, key);
    return result;
//    return (*env)->NewStringUTF(env,"返回为空2");
}