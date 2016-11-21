package com.souche.android.fcadapter.demo.entity;

import com.souche.android.sdk.fcadapter.item.SectionItem;

public class MySection extends SectionItem<Video> {
    private boolean isMore;
    public MySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySection(Video t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
