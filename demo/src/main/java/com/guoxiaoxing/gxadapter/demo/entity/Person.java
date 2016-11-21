package com.guoxiaoxing.gxadapter.demo.entity;

import com.guoxiaoxing.gxadapter.demo.adapter.ExpandableItemAdapter;
import com.guoxiaoixng.gxadapter.item.MultiItem;

public class Person implements MultiItem {
    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public String name;
    public int age;

    @Override
    public int getItemType() {
        return ExpandableItemAdapter.TYPE_PERSON;
    }
}