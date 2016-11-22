package com.guoxiaoxing.gxadapter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guoxiaoixng.gxadapter.GXAdapter;
import com.guoxiaoixng.gxadapter.listener.FCItemClickListener;
import com.guoxiaoxing.gxadapter.demo.adapter.HomeAdapter;
import com.guoxiaoxing.gxadapter.demo.entity.HomeItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final Class<?>[] ACTIVITY = {
            AnimationActivity.class,
            MultipleItemActivity.class,
            HeaderAndFooterActivity.class,
            PullToRefreshActivity.class,
            SectionActivity.class,
            EmptyViewActivity.class,
            DragAndSwipeActivity.class,
            RecyclerClickItemActivity.class,
            ExpandableActivity.class,
            StickyHeaderActivity.class};

    private static final String[] TITLE = {
            "loading animation",
            "multiple item",
            "add header and footer",
            "pull to refresh, push up to loading",
            "expand and collpase",
            "empty view",
            "drag, swipe dismiss, swipe menu",
            "item click event",
            "expandable item",
            "sticky header"};

    private ArrayList<HomeItem> mDataList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initData();
        GXAdapter homeAdapter = new HomeAdapter(R.layout.home_item_view, mDataList);
        homeAdapter.openLoadAnimation();
        mRecyclerView.addOnItemTouchListener(new FCItemClickListener() {
            @Override
            public void SimpleOnItemClick(GXAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, ACTIVITY[position]);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(homeAdapter);
    }

    private void initData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomeItem item = new HomeItem();
            item.setTitle(TITLE[i]);
            item.setActivity(ACTIVITY[i]);
            mDataList.add(item);
        }
    }
}
