package com.guoxiaoxing.gxadapter.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guoxiaoixng.gxadapter.FCAdapter;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.adapter.DemoFCAdapter;
import com.guoxiaoxing.gxadapter.demo.data.DataServer;
import com.guoxiaoixng.gxadapter.fastscroller.FastScroller;
import com.guoxiaoixng.gxadapter.listener.FCItemClickListener;


public class PullToRefreshActivity extends AppCompatActivity implements FastScroller.OnScrollStateChangeListener {
    private RecyclerView mRecyclerView;
    private DemoFCAdapter mFCDemoFCAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FastScroller mFastScroller;

    private static final int NETWORK_REQUEST_PAGE_SIZE = 10;

    private static final int LIST_MAX_SIZE = 20;

    private int delayMillis = 1000;

    private boolean isErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mFastScroller = (FastScroller) findViewById(R.id.baselib_fast_scroller);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        addHeadView();
        mRecyclerView.setAdapter(mFCDemoFCAdapter);
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        mFCDemoFCAdapter.addHeaderView(headView);
    }

    private void setupAdapter() {
        mFCDemoFCAdapter = new DemoFCAdapter(NETWORK_REQUEST_PAGE_SIZE);
        mFCDemoFCAdapter.openLoadAnimation();

        mRecyclerView.setAdapter(mFCDemoFCAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFCDemoFCAdapter.onRefreshSuccess(DataServer.getSampleData(0));
                        mFCDemoFCAdapter.removeAllFooterView();
                        mSwipeRefreshLayout.setRefreshing(false);
                        isErr = false;
                    }
                }, delayMillis);
            }
        });
        mFCDemoFCAdapter.setOnLoadMoreListener(new FCAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isErr) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mFCDemoFCAdapter.getData().size() <= LIST_MAX_SIZE) {
                                        mFCDemoFCAdapter.onLoadMoreSucess(DataServer.getSampleData(NETWORK_REQUEST_PAGE_SIZE));
                                    } else {
                                        mFCDemoFCAdapter.onLoadMoreSucess(DataServer.getSampleData(6));
                                    }
                                }
                            }, delayMillis);
                        } else {
                            isErr = true;
                            mFCDemoFCAdapter.onLoadMoreFailed();
                        }
                    }
                });
            }
        });


        mRecyclerView.addOnItemTouchListener(new FCItemClickListener() {
            @Override
            public void SimpleOnItemClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(PullToRefreshActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
        setupFastScroller();
    }

    private void setupFastScroller() {
        mFastScroller.setRecyclerView(mRecyclerView);
        mFastScroller.addOnScrollStateChangeListener(this);
        mFastScroller.setViewsToUse(
                R.layout.baselib_fast_scroller_bubble,
                R.id.fast_scroller_bubble,
                R.id.fast_scroller_handle, Color.parseColor("#FF571A"));
    }

    @Override
    public void onFastScrollerStateChange(boolean scrolling) {

    }
}
