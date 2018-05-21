package com.example.simple.simpleandroidpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.simple.simpleandroidpractice.MyJNI.MyJni;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String netRsaKey = MyJni.getNetRsaKey();
        Log.i(TAG,"key = "+netRsaKey);
    }
}
