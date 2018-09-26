package com.example.simple.simpleandroidpractice.loading;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simple.simpleandroidpractice.R;
import com.example.simple.simpleandroidpractice.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：
 * author：LiaoYangSen
 * email：lyssenlove@sina.com
 * create：2018/9/26
 */
public class LoadingFragment extends BaseFragment {

    @BindView(R.id.btn_download_start)
    Button mBtnStart;
    @BindView(R.id.loading_view)
    LoadingView mLoadingView;

    public static LoadingFragment newInstance() {
        LoadingFragment fragment = new LoadingFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_download_start)
    void onViewClick(View view){
        switch (view.getId()){
            case R.id.btn_download_start:
                mLoadingView.start();
                break;
        }
    }

}
