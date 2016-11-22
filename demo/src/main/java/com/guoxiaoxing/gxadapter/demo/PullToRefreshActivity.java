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

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.fastscroller.GXFastScroller;
import com.guoxiaoxing.gxadapter.listener.GXItemClickListener;
import com.guoxiaoxing.gxadapter.demo.adapter.DemoGXAdapter;
import com.guoxiaoxing.gxadapter.demo.data.DataServer;


public class PullToRefreshActivity extends AppCompatActivity implements GXFastScroller.OnScrollStateChangeListener {
    private static final int NETWORK_REQUEST_PAGE_SIZE = 10;
    private static final int LIST_MAX_SIZE = 20;
    private RecyclerView mRecyclerView;
    private DemoGXAdapter mFCDemoFCAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GXFastScroller mGXFastScroller;
    private int delayMillis = 1000;

    private boolean isErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mGXFastScroller = (GXFastScroller) findViewById(R.id.baselib_fast_scroller);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        addHeadView();
        mRecyclerView.setAdapter(mFCDemoFCAdapter);
    }

    private void setupAdapter() {
        mFCDemoFCAdapter = new DemoGXAdapter(NETWORK_REQUEST_PAGE_SIZE);
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
        mFCDemoFCAdapter.setOnLoadMoreListener(new GXAdapter.OnLoadMoreListener() {
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


        mRecyclerView.addOnItemTouchListener(new GXItemClickListener() {
            @Override
            public void SimpleOnItemClick(GXAdapter adapter, View view, int position) {
                Toast.makeText(PullToRefreshActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
        setupFastScroller();
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        mFCDemoFCAdapter.addHeaderView(headView);
    }

    private void setupFastScroller() {
        mGXFastScroller.setRecyclerView(mRecyclerView);
        mGXFastScroller.addOnScrollStateChangeListener(this);
        mGXFastScroller.setViewsToUse(
                R.layout.baselib_fast_scroller_bubble,
                R.id.fast_scroller_bubble,
                R.id.fast_scroller_handle, Color.parseColor("#FF571A"));
    }

    @Override
    public void onFastScrollerStateChange(boolean scrolling) {

    }
}
