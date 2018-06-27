package com.example.simple.simpleandroidpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.simple.simpleandroidpractice.videoPlay.VideoPlayActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = VideoPlayActivity.newIntent(MainActivity.this);
        startActivity(intent);
    }
}
