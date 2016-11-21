package com.souche.android.fcadapter.demo.entity;

import com.souche.android.fcadapter.demo.adapter.ExpandableItemAdapter;
import com.souche.android.sdk.fcadapter.item.AbstractExpandableItem;
import com.souche.android.sdk.fcadapter.item.MultiItem;

public class Level0Item extends AbstractExpandableItem<Level1Item> implements MultiItem {
    public String title;
    public String subTitle;

    public Level0Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }
}
