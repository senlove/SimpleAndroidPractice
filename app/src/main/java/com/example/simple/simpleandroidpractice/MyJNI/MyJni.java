package com.example.simple.simpleandroidpractice.MyJNI;

public class MyJni {

    static {
        System.loadLibrary("MyJni");
    }


    public static native String getNetRsaKey();

}
