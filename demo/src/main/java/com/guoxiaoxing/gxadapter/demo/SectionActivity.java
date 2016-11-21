package com.guoxiaoxing.gxadapter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.guoxiaoixng.gxadapter.FCAdapter;
import com.guoxiaoxing.gxadapter.demo.R;
import com.guoxiaoxing.gxadapter.demo.adapter.SectionAdapter;
import com.guoxiaoxing.gxadapter.demo.data.DataServer;
import com.guoxiaoxing.gxadapter.demo.entity.MySection;
import com.guoxiaoixng.gxadapter.listener.FCItemClickListener;

import java.util.List;

public class SectionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<MySection> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mData = DataServer.getSampleData();
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);
        mRecyclerView.addOnItemTouchListener(new FCItemClickListener() {

            @Override
            public void SimpleOnItemClick(FCAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader)
                    Toast.makeText(SectionActivity.this, mySection.header, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SectionActivity.this, mySection.t.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemChildClick(FCAdapter adapter, View view, int position) {
                Toast.makeText(SectionActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }


        });
        mRecyclerView.setAdapter(sectionAdapter);
    }


}
