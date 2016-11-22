package com.guoxiaoxing.gxadapter.demo.entity;


import com.guoxiaoxing.gxadapter.demo.adapter.ExpandableItemAdapter;
import com.guoxiaoxing.gxadapter.item.AbstractExpandableItem;
import com.guoxiaoxing.gxadapter.item.MultiItem;

public class Level1Item extends AbstractExpandableItem<Person> implements MultiItem {
    public String title;
    public String subTitle;

    public Level1Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_LEVEL_1;
    }
}