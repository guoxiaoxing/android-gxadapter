package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.guoxiaoxing.gxadapter.GXAdapter;
import com.guoxiaoxing.gxadapter.listener.GXItemClickListener;
import com.guoxiaoxing.gxadapter.demo.adapter.ClickAdapter;

public class RecyclerClickItemActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 10;
    private static String TAG = "RecyclerClickItemActivity";
    private RecyclerView mRecyclerView;
    private ClickAdapter mQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_click);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mQuickAdapter.addHeaderView(getHeadView());
        mQuickAdapter.addFooterView(getFootView());

//        /**
//         * Item  clcik
//         */
//
        mRecyclerView.addOnItemTouchListener(new GXItemClickListener() {

            @Override
            public void onItemLongClick(GXAdapter adapter, View view, int position) {
                super.onItemLongClick(adapter, view, position);
                Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " Item is LongClick ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemChildClick(GXAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.tweetAvatar:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " tweetAvatar  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tweetName:
                        Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + " tweetName  is clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(GXAdapter adapter, View view, int position) {
                super.onItemChildLongClick(adapter, view, position);
                Toast.makeText(RecyclerClickItemActivity.this, "The " + Integer.toString(position) + "  view itemchild " + "is LongClick " + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void SimpleOnItemClick(GXAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * this is sample code
         */
   /*     mRecyclerView.addOnItemTouchListener(new GXItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView.addOnItemTouchListener(new GXItemLongClickListener() {
            @Override
            public void SimpleOnItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerView.addOnItemTouchListener(new GXItemChildLongClickListener() {
            @Override
            public void SimpleOnItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.addOnItemTouchListener(new GXClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(RecyclerClickItemActivity.this, "" + Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initAdapter() {
        mQuickAdapter = new ClickAdapter(PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
    }

    private View getHeadView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click headView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private View getFootView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerClickItemActivity.this, "click FootView", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
