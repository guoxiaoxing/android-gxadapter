package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.guoxiaoxing.gxadapter.demo.entity.LineItem;
import com.guoxiaoxing.gxadapter.demo.sticky.StickyHeaderAdapter;
import com.guoxiaoxing.gxadapter.sticky.StickyLayoutManager;

import java.util.ArrayList;

public class StickyHeaderActivity extends AppCompatActivity {

    private RecyclerView mRvStickyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_header);

        mRvStickyHeader = (RecyclerView) findViewById(R.id.rv_sticky_header);

        mRvStickyHeader.setLayoutManager(new StickyLayoutManager(StickyHeaderActivity.this));

        String[] countryNames = getResources().getStringArray(R.array.country_names);
        ArrayList<LineItem> list = new ArrayList<>();

        LineItem headerItem = new LineItem();
        headerItem.setText("Header");
        headerItem.setItemType(StickyHeaderAdapter.VIEW_TYPE_HEADER);
        list.add(headerItem);

        for (String countryName : countryNames) {
            LineItem contentItem = new LineItem();
            contentItem.setText(countryName);
            contentItem.setItemType(StickyHeaderAdapter.VIEW_TYPE_CONTENT);
            list.add(contentItem);
        }

        StickyHeaderAdapter stickyHeaderAdapter = new StickyHeaderAdapter(list);
        mRvStickyHeader.setAdapter(stickyHeaderAdapter);
    }
}
