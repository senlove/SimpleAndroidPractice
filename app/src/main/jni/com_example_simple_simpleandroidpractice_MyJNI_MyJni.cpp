
#include "com_example_simple_simpleandroidpractice_MyJNI_MyJni.h"
/*
 * Class:     com_example_simple_simpleandroidpractice_MyJNI_MyJni
 * Method:    getNetRsaKey
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_simple_simpleandroidpractice_MyJNI_MyJni_getNetRsaKey
  (JNIEnv *env, jobject obj){

    return env -> NewStringUTF("from cpp");
  }

