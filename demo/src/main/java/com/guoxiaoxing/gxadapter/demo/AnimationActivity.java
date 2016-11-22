package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.guoxiaoixng.gxadapter.GXAdapter;
import com.guoxiaoixng.gxadapter.listener.FCItemChildClickListener;
import com.guoxiaoxing.gxadapter.demo.adapter.DemoGXAdapter;
import com.guoxiaoxing.gxadapter.demo.animation.CustomAnimation;
import com.guoxiaoxing.gxadapter.demo.entity.Status;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class AnimationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DemoGXAdapter mQuickDemoFCAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initMenu();
    }

    private void initAdapter() {
        mQuickDemoFCAdapter = new DemoGXAdapter();
        mQuickDemoFCAdapter.openLoadAnimation();
        mRecyclerView.addOnItemTouchListener(new FCItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(GXAdapter adapter, View view, int position) {
                String content = null;
                Status status = (Status) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tweetAvatar:
                        content = "img:" + status.getUserAvatar();
                        break;
                    case R.id.tweetName:
                        content = "name:" + status.getUserName();
                        break;
                }
                Toast.makeText(AnimationActivity.this, content, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mQuickDemoFCAdapter);
    }

    private void initMenu() {
        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("AlphaIn", "ScaleIn", "SlideInBottom", "SlideInLeft", "SlideInRight", "Custom");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mQuickDemoFCAdapter.openLoadAnimation(GXAdapter.ANIMATION_TYPE_ALPHA);
                        break;
                    case 1:
                        mQuickDemoFCAdapter.openLoadAnimation(GXAdapter.ANIMATION_TYPE_SCALE);
                        break;
                    case 2:
                        mQuickDemoFCAdapter.openLoadAnimation(GXAdapter.ANIMATION_TYPE_SLIDE_BOTTOM);
                        break;
                    case 3:
                        mQuickDemoFCAdapter.openLoadAnimation(GXAdapter.ANIMATION_TYPE_SLIDE_LEFT);
                        break;
                    case 4:
                        mQuickDemoFCAdapter.openLoadAnimation(GXAdapter.ANIMATION_TYPE_SLIDE_RIGHT);
                        break;
                    case 5:
                        mQuickDemoFCAdapter.openLoadAnimation(new CustomAnimation());
                        break;
                    default:
                        break;
                }
                mRecyclerView.setAdapter(mQuickDemoFCAdapter);
            }
        });
        MaterialSpinner spinnerFirstOnly = (MaterialSpinner) findViewById(R.id.spinner_first_only);
        spinnerFirstOnly.setItems("isFirstOnly(true)", "isFirstOnly(false)");
        spinnerFirstOnly.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                switch (position) {
                    case 0:
                        mQuickDemoFCAdapter.isFirstOnly(true);
                        break;
                    case 1:
                        mQuickDemoFCAdapter.isFirstOnly(false);
                        break;
                    default:
                        break;
                }
                mQuickDemoFCAdapter.notifyDataSetChanged();
            }
        });
    }

}
