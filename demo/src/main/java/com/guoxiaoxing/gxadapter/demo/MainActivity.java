package com.guoxiaoxing.gxadapter.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.demo.adapter.HomeAdapter;
import com.guoxiaoxing.gxadapter.demo.entity.HomeItem;
import com.guoxiaoxing.gxadapter.listener.GXItemClickListener;

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
        setupView();
        setupData();
    }

    private void setupView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.addOnItemTouchListener(new GXItemClickListener() {
            @Override
            public void SimpleOnItemClick(GXAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, ACTIVITY[position]);
                startActivity(intent);
            }
        });
    }

    private void setupData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < TITLE.length; i++) {
            HomeItem item = new HomeItem();
            item.setTitle(TITLE[i]);
            item.setActivity(ACTIVITY[i]);
            mDataList.add(item);
        }

        HomeAdapter homeAdapter = new HomeAdapter(R.layout.home_item_view, mDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(homeAdapter);
    }
}
