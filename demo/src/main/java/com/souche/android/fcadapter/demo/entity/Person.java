package com.souche.android.fcadapter.demo.entity;

import com.souche.android.fcadapter.demo.adapter.ExpandableItemAdapter;
import com.souche.android.sdk.fcadapter.item.MultiItem;

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