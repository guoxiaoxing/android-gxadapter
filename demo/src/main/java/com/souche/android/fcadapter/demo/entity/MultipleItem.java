package com.souche.android.fcadapter.demo.entity;


import com.souche.android.sdk.fcadapter.item.MultiItem;

public class MultipleItem implements MultiItem {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int BIG_IMG_SPAN_SIZE = 3;
    public static final int TEXT_SPAN_SIZE = 3;
    public static final int IMG_SPAN_SIZE = 1;
    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
