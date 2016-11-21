package com.souche.android.sdk.fcadapter.item;

public abstract class SectionItem<T> {
    public boolean isHeader;
    public T t;
    public String header;

    public SectionItem(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionItem(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
