package com.example.simple.simpleandroidpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.simple.simpleandroidpractice.listLoadmore.ListLoadmoreFragment;
import com.example.simple.simpleandroidpractice.loading.LoadingFragment;
import com.example.simple.simpleandroidpractice.videoPlay.VideoPlayActivity;

import me.yokeyword.fragmentation.SupportActivity;

public class MainActivity extends SupportActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadingFragment fragment = LoadingFragment.newInstance();
        loadRootFragment(R.id.fl_main, fragment);

//        Intent intent = VideoPlayActivity.newIntent(MainActivity.this);
//        startActivity(intent);

    }
}
