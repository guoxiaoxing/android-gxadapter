package com.souche.android.fcadapter.demo.entity;


import com.souche.android.sdk.fcadapter.item.MultiItem;

public class LineItem implements MultiItem {

    private String text;
    private int itemType;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}