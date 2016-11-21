package com.guoxiaoxing.gxadapter.demo.entity;

import com.guoxiaoxing.gxadapter.demo.adapter.ExpandableItemAdapter;
import com.guoxiaoixng.gxadapter.item.AbstractExpandableItem;
import com.guoxiaoixng.gxadapter.item.MultiItem;

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
