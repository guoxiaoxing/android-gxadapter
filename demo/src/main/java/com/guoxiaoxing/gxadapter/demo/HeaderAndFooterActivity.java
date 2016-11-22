package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.listener.GXItemClickListener;
import com.guoxiaoxing.gxadapter.demo.adapter.DemoGXAdapter;

public class HeaderAndFooterActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 3;
    private RecyclerView mRecyclerView;
    private DemoGXAdapter mQuickDemoFCAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_and_footer_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();

        View headerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickDemoFCAdapter.addHeaderView(getView(getRemoveHeaderListener(), "click me to remove me"), 0);
            }
        }, "click me to add new header");
        mQuickDemoFCAdapter.addHeaderView(headerView);

        View footerView = getView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickDemoFCAdapter.addFooterView(getView(getRemoveFooterListener(), "click me to remove me"));
            }
        }, "click me to add new footer");
        mQuickDemoFCAdapter.addFooterView(footerView, 0);

        mRecyclerView.setAdapter(mQuickDemoFCAdapter);
    }

    private void initAdapter() {
        mQuickDemoFCAdapter = new DemoGXAdapter(PAGE_SIZE);
        mQuickDemoFCAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickDemoFCAdapter);
        mRecyclerView.addOnItemTouchListener(new GXItemClickListener() {
            @Override
            public void SimpleOnItemClick(GXAdapter adapter, View view, int position) {
                Toast.makeText(HeaderAndFooterActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    private View getView(View.OnClickListener listener, String text) {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ((TextView) view.findViewById(R.id.tv)).setText(text);
        view.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener getRemoveHeaderListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickDemoFCAdapter.removeHeaderView(v);
            }
        };
    }

    private View.OnClickListener getRemoveFooterListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickDemoFCAdapter.removeFooterView(v);
            }
        };
    }

    private View getView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HeaderAndFooterActivity.this, "click View", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}
