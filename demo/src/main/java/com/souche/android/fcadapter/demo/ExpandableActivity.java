package com.souche.android.fcadapter.demo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.souche.android.fcadapter.demo.adapter.ExpandableItemAdapter;
import com.souche.android.fcadapter.demo.entity.Level0Item;
import com.souche.android.fcadapter.demo.entity.Level1Item;
import com.souche.android.fcadapter.demo.entity.Person;
import com.souche.android.sdk.fcadapter.item.MultiItem;

import java.util.ArrayList;
import java.util.Random;

public class ExpandableActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_item_use);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<MultiItem> list = generateData();
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(list);

        View headerView = getView();
        adapter.addHeaderView(headerView);

        View footerView = getView();
        adapter.addFooterView(footerView);

        mRecyclerView.setAdapter(adapter);

    }


    private View getView() {
        View view = getLayoutInflater().inflate(R.layout.head_view, null);
        view.findViewById(R.id.tv).setVisibility(View.GONE);
        view.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExpandableActivity.this, "click View", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }


    private ArrayList<MultiItem> generateData() {
        int lv0Count = 9;
        int lv1Count = 3;
        int personCount = 5;

        String[] nameList = {"Bob", "Andy", "Lily", "Brown", "Bruce"};
        Random random = new Random();

        ArrayList<MultiItem> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            Level0Item lv0 = new Level0Item("This is " + i + "th item in Level 0", "subtitle of " + i);
            for (int j = 0; j < lv1Count; j++) {
                Level1Item lv1 = new Level1Item("Level 1 item: " + j, "(no animation)");
                for (int k = 0; k < personCount; k++) {
                    lv1.addSubItem(new Person(nameList[k], random.nextInt(40)));
                }
                lv0.addSubItem(lv1);
            }
            res.add(lv0);
        }
        return res;
    }
}
