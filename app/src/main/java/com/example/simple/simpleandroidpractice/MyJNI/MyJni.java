package com.example.simple.simpleandroidpractice.MyJNI;

public class MyJni {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String getNetRsaKey();

}
