package com.example.simple.simpleandroidpractice.listLoadmore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.simple.simpleandroidpractice.R;
import com.example.simple.simpleandroidpractice.base.BaseFragment;
import com.example.simple.simpleandroidpractice.base.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 1.显示加载更多的ui效果
 * 2.提供加载更多的回调接口进行数据的加载
 * 3.对于RecyclerView不同的LayoutManage提供加载功能
 *
 * 面临的情况
 * RecyclerView 嵌套在scrollview的情况
 * RecyclerView 数据不铺满屏幕的情况
 *
 */
public class ListLoadmoreFragment extends BaseFragment{

    private static final String TAG = "ListLoadmoreFragment";

    @BindView(R.id.rv_list_load_more)
    RecyclerView mRv;
    private ArrayList<String> mDataList;

    public static ListLoadmoreFragment newInstance() {
        ListLoadmoreFragment fragment = new ListLoadmoreFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_load_more;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(linearLayoutManager);

        mDataList = new ArrayList<>();
        for(int i=0; i<20; i++){
            mDataList.add("item "+i);
        }

        //负责了数据的加载以及界面的显示
        MyListAdapter adapter = new MyListAdapter();
        adapter.setList(mDataList);
        mRv.setAdapter(adapter);
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if(layoutManager instanceof LinearLayoutManager){
                        LinearLayoutManager innerLinearLayoutManager = (LinearLayoutManager) layoutManager;
                        int lastVisibleItemPosition = innerLinearLayoutManager.findLastVisibleItemPosition();
                        int lastCompletelyVisibleItemPosition = innerLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = innerLinearLayoutManager.getItemCount();
                        Log.i(TAG, "lastVisibleItemPosition = "+lastVisibleItemPosition);
                        Log.i(TAG, "lastCompletelyVisibleItemPosition = "+lastCompletelyVisibleItemPosition);
                        Log.i(TAG, "totalItemCount = "+totalItemCount);
                        // 判断是否滚动到底部，并且是向右滚动
                        if (lastCompletelyVisibleItemPosition == (totalItemCount - 1)) {
                            Log.i(TAG, "滑动到最后");
                            //显示loading的UI
                            //加载更多功能的代码
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    static class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_LOAD_MORE = 1000;

        private List<String> mList;
        private boolean isLoadmore;

        @Override
        public int getItemViewType(int position) {
            if(position == mList.size()){
                return TYPE_LOAD_MORE;
            }
            return super.getItemViewType(position);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if(viewType == TYPE_LOAD_MORE){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
                return new MyListLoadMoreViewHolder(view);
            }
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new MyListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(TYPE_LOAD_MORE != getItemViewType(position)){
                MyListViewHolder myListViewHolder = (MyListViewHolder) holder;
                String item = mList.get(position);
                myListViewHolder.mTv.setText(item);
            } else {
                MyListLoadMoreViewHolder myListLoadMoreViewHolder = (MyListLoadMoreViewHolder) holder;
                myListLoadMoreViewHolder.mRlLoadmoreContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return null == mList ? 0 : mList.size() + 1;
        }

        public List<String> getList() {
            return mList;
        }

        public void setList(List<String> list) {
            mList = list;
        }

        static class MyListViewHolder extends CommonViewHolder{
            @BindView(R.id.tv_item_list)
            TextView mTv;
            public MyListViewHolder(View itemView) {
                super(itemView);
            }
        }

        static class MyListLoadMoreViewHolder extends CommonViewHolder{

            @BindView(R.id.rl_list_load_more)
            RelativeLayout mRlLoadmoreContainer;

            public MyListLoadMoreViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

}
