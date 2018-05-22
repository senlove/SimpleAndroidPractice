//
// Created by win10 on 2018/5/22.
//

#include <jni.h>
#include <string>

extern "C"


JNIEXPORT jstring JNICALL Java_com_example_simple_simpleandroidpractice_MyJNI_MyJni_getNetRsaKey
  (JNIEnv *env, jobject obj){

    return env -> NewStringUTF("from cpp");
  }



