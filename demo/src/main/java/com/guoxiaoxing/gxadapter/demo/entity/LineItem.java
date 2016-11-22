package com.guoxiaoxing.gxadapter.demo.entity;


import com.guoxiaoxing.gxadapter.item.MultiItem;

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