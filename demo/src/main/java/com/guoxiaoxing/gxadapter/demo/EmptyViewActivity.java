package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.adapter.DemoFCAdapter;

public class EmptyViewActivity extends AppCompatActivity {
    private SwipeRefreshLayout mRvRefresh;
    private RecyclerView mRecyclerView;
    private DemoFCAdapter mDemoFCAdapter;
//    private EmptyLayout mEmptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view_use);
        mRvRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDemoFCAdapter = new DemoFCAdapter(10);

//        mEmptyLayout = new EmptyLayout(EmptyViewActivity.this);
//        RecyclerView.LayoutParams params = new GridStickyLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        mEmptyLayout.setLayoutParams(params);
//        mDemoFCAdapter.setEmptyView(mEmptyLayout);
//        mRecyclerView.setAdapter(mDemoFCAdapter);
//
//        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEmptyLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mDemoFCAdapter.onRefreshSuccess(DataServer.getSampleData(10));
//                    }
//                }, 1000);
//            }
//        });
//
//        mRvRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRvRefresh.setRefreshing(false);
//                        mDemoFCAdapter.onRefreshSuccess(null);
//                        mEmptyLayout.showEmpty();
//                    }
//                }, 1000);
//
//            }
//        });
    }
}
