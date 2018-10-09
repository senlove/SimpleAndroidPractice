package com.example.simple.simpleandroidpractice.knowledgePoint.Animate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.simple.simpleandroidpractice.R;
import com.example.simple.simpleandroidpractice.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：
 * author：LiaoYangSen
 * email：lyssenlove@sina.com
 * create：2018/10/9
 */
public class ViewAnimateFragment extends BaseFragment {

    @BindView(R.id.tv_view_animate)
    TextView mTv;

    public static ViewAnimateFragment newInstance() {
        ViewAnimateFragment fragment = new ViewAnimateFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_animate;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_view_animate)
    void onViewClick(View view){
        mTv.animate().scaleY(1.5f).scaleX(1.5f).setDuration(500).start();
    }

}
